package nl.utwente.soa.meeting_service.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.meeting_service.access.MeetingTaskRepository;
import nl.utwente.soa.meeting_service.access.MeetingRepository;
import nl.utwente.soa.meeting_service.model.MeetingTask;
import nl.utwente.soa.meeting_service.model.Goal;
import nl.utwente.soa.meeting_service.model.Meeting;
import nl.utwente.soa.meeting_service.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MeetingService {

  private final MeetingRepository meetingRepository;
  private final MeetingTaskRepository meetingTaskRepository;
  private final JmsTemplate jmsTemplate;
  @Value("${ActiveMQ.queue.meeting}")
  private String meetingQueue;
  @Value("${service.project_service}")
  private String projectService;

  @Autowired
  public MeetingService(JmsTemplate jmsTemplate, MeetingRepository meetingRepository, MeetingTaskRepository meetingTaskRepository) {
    this.meetingRepository = meetingRepository;
    this.meetingTaskRepository = meetingTaskRepository;
    this.jmsTemplate = jmsTemplate;
  }

  public List<Meeting> getMeetings(Long projectId, Long goalId) {
    return meetingRepository.findAllByProjectIdAndGoalId(projectId, goalId);
  }

  public Meeting getMeeting(Long projectId, Long goalId, Long meetingId){
    return meetingRepository.findMeetingByProjectIdAndGoalIdAndMeetingId(
            projectId,
            goalId,
            meetingId
    ).orElseThrow(() -> new IllegalStateException(
                    "Meeting with Id " + meetingId + " does not exist within for goal with goalId " + goalId +
                    " within project " + projectId + "."
            )
    );
  }

  public void createNewMeeting(Long projectId, Long goalId, Meeting meeting) {
    // throw an exception if not all required fields are filled in
    if (meeting.getDate() == null) {
      throw new IllegalStateException("The following fields cannot be null: Date");
    }
    // throw an exception if an id is specified in the body of the POST request (they should be created by the server)
    if (meeting.getProjectId() != null || meeting.getGoalId() != null ||
        meeting.getMeetingId() != null || meeting.getId() != null
        || meeting.getJoinLink() != null) {
      throw new IllegalStateException(
              "Please remove the following fields from the body of the POST request: " +
                      "projectId, goalId, taskId, id, joinLink"
      );
    }
    // throw an exception if the meeting date is created with a date later than the goal deadline
    Date plannedDate = meeting.getDate();
    Date goalDeadline = this.getGoal(projectId, goalId).getDeadline();
    if (plannedDate.after(goalDeadline)) {
      throw new IllegalStateException("The meeting date cannot be later than the goal deadline!");
    }
    // Calculate max MeetingId
    List<Meeting> plannedMeetings = this.getMeetings(projectId, goalId);
    Long maxMeetingId = 0L;
    for (Meeting plannedMeeting : plannedMeetings) {
      if (plannedMeeting.getMeetingId() > maxMeetingId) {
        maxMeetingId = plannedMeeting.getMeetingId();
      }
    }
    // make sure these fields are set, because they are not specified in the body of the POST request
    meeting.setMeetingId(maxMeetingId + 1);
    meeting.setGoalId(goalId);
    meeting.setProjectId(projectId);
    meetingRepository.save(meeting);

    // send created meeting via MQ to the JoinLinkService
    jmsTemplate.convertAndSend(meetingQueue, meeting);
  }

  @Transactional
  public void deleteMeeting(Long projectId, Long goalId, Long meetingId) {
    // throw an exception if the goal of the meeting does not exist
    Optional<Meeting> meeting = meetingRepository.findMeetingByProjectIdAndGoalIdAndMeetingId(
            projectId,
            goalId,
            meetingId
    );
    if (meeting.isEmpty()) {
      throw new IllegalStateException(
              "Meeting with Id " + meetingId + " does not exist within this goal within this project."
      );
    } else {
      // only planned meetings can be deleted (cancelled). Meetings that already happened should persist
      Date currentDate = new Date();
      if (this.getMeeting(projectId, goalId, meetingId).getDate().after(currentDate)) {
        // delete all tasks discussed at this meeting
        meetingTaskRepository.deleteAllByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId);
        // delete the meeting itself
        meetingRepository.deleteMeetingByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId);
      } else {
        throw new IllegalStateException("You cannot delete a meeting that has already happened! " +
                "You can only delete meetings that are planned but have not happened yet.");
      }
    }
  }

  @Transactional
  public void rescheduleMeeting(Long projectId, Long goalId, Long meetingId, Date date) {
    Meeting meeting = meetingRepository.findMeetingByProjectIdAndGoalIdAndMeetingId(
        projectId, goalId, meetingId).orElseThrow(() -> new IllegalStateException(
        "Meeting with id " + meetingId + " does not exist within this goal within this project"
    ));

    if (date != null && !Objects.equals(meeting.getDate(), date)) {
      // throw an exception if the meeting deadline is updated to a date later than the goal deadline
      Date goalDeadline = this.getGoal(projectId, goalId).getDeadline();
      if (date.after(goalDeadline)) {
        throw new IllegalStateException("The meeting date cannot be later than the goal deadline!");
      }
      meeting.setDate(date);
    }
  }

  @Transactional
  public void addJoinLinkToMeeting(Long projectId, Long goalId, Long meetingId, String joinLink) {
    Meeting meeting = meetingRepository.findMeetingByProjectIdAndGoalIdAndMeetingId(
            projectId,
            goalId,
            meetingId
    ).orElseThrow(() -> new IllegalStateException(
                    "Meeting with Id " + meetingId + " does not exist within for goal with goalId " + goalId +
                            " within project " + projectId + "."
            )
    );

    if (joinLink == null) {
      throw new IllegalStateException("JoinLink cannot be null! Check the JoinLink service");
    } else {
      meeting.setJoinLink(joinLink);
    }
  }

  /*
  The following operations are about tasks to be discussed during a meeting
   */

  public List<Long> getTasksOfMeeting(Long projectId, Long goalId, Long meetingId) {
    // throw an exception if the meeting of the meetingTasks does not exist
    Meeting meeting = this.getMeeting(projectId, goalId, meetingId);
    List<MeetingTask> meetingTasks = meetingTaskRepository.findAllByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId);
    List<Long> meetingTaskIds = new ArrayList<>();
    for(MeetingTask meetingTask : meetingTasks) {
      meetingTaskIds.add(meetingTask.getTaskId());
    }
    return meetingTaskIds;
  }

  public void addTaskToMeeting(Long projectId, Long goalId, Long meetingId, Long taskId) {
    Meeting meeting = this.getMeeting(projectId, goalId, meetingId);
    Task task = this.getTask(projectId, goalId, taskId);
    Date currentDate = new Date();
    if (meeting.getDate().after(currentDate)) {
      MeetingTask meetingTask = new MeetingTask(meetingId, taskId, projectId, goalId, null);
      meetingTaskRepository.save(meetingTask);
    } else {
      throw new IllegalStateException("No tasks can be added to a meeting that has already happened.");
    }
  }

  @Transactional
  public void deleteTaskFromMeeting(Long projectId, Long goalId, Long meetingId, Long taskId) {
    Meeting meeting = this.getMeeting(projectId, goalId, meetingId);
    MeetingTask meetingTask = meetingTaskRepository.findDiscussedByProjectIdAndGoalIdAndMeetingIdAndTaskId(
            projectId, goalId, meetingId, taskId
    ).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist within this meeting"
    ));
    meetingTaskRepository.deleteDiscussedByProjectIdAndGoalIdAndMeetingIdAndTaskId(projectId, goalId, meetingId, taskId);
  }


  @Autowired
  private RestTemplateBuilder restTemplateBuilder;
  @Autowired
  private RestTemplateBuilder restTemplateBuilder2;

  // Get information from project_service

  public Goal getGoal(Long projectId, Long goalId) {
    String url = projectService + "api/projects/" + projectId + "/goals/" + goalId;
    RestTemplate restTemplate = restTemplateBuilder.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
    return restTemplate.getForObject(url, Goal.class);
  }

  public Task getTask(Long projectId, Long goalId, Long taskId) {
    String url = projectService + "api/projects/" + projectId + "/goals/" + goalId + "/tasks/" + taskId;
    RestTemplate restTemplate = restTemplateBuilder2.build();
    return restTemplate.getForObject(url, Task.class);
  }

  public boolean checkGoalExists(Long projectId, Long goalId) {
    // throw an exception if the goal of the meeting does not exist
    Goal goal = this.getGoal(projectId, goalId);
    if (goal == null) {
      throw new IllegalStateException("Requested goal with goalId " + goalId + " for project with projectId" + projectId);
    } else {
      return true;
    }
  }
}
