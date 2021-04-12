package nl.utwente.soa.meeting_service.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.transaction.Transactional;
import nl.utwente.soa.meeting_service.access.DiscussedRepository;
import nl.utwente.soa.meeting_service.access.MeetingRepository;
import nl.utwente.soa.meeting_service.model.Discussed;
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
  private final DiscussedRepository discussedRepository;
  private final JmsTemplate jmsTemplate;
  @Value("${ActiveMQ.queue.meeting}")
  private String meetingQueue;
  @Value("${service.project_service}")
  private String projectService;

  @Autowired
  public MeetingService(JmsTemplate jmsTemplate, MeetingRepository meetingRepository, DiscussedRepository discussedRepository) {
    this.meetingRepository = meetingRepository;
    this.discussedRepository = discussedRepository;
    this.jmsTemplate = jmsTemplate;
  }

  public List<Meeting> getMeetings(Long projectId, Long goalId) {
    // throw an exception if the goal of the meetings does not exist
    try {
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    return meetingRepository.findAllByProjectIdAndGoalId(projectId, goalId);
  }

  public Optional<Meeting> getMeeting(Long projectId, Long goalId, Long meetingId){
    // throw an exception if the goal of the meeting does not exist
    try {
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    Meeting meeting = meetingRepository.findMeetingByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId).orElseThrow(() -> new IllegalStateException(
        "Meeting with Id " + meetingId + " does not exist within this goal within this project."
    ));
    return meetingRepository.findMeetingByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId);
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
      throw new IllegalStateException("Please remove the following fields from the body of " +
          "the POST request: projectId, goalId, taskId, id, joinLink");
      // The value of the DB id actually doesn't matter, since it is overwritten by the sequence_generator
    }
    // throw an exception if either the project or the goal of the to-be-created meeting does not exist
    try {
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    List<Meeting> plannedMeetings = this.getMeetings(projectId, goalId);
    Long max = 0L;
    for (Meeting plannedMeeting : plannedMeetings) {
      if (plannedMeeting.getMeetingId() > max) {
        max = plannedMeeting.getMeetingId();
      }
    }
    // throw an exception if the meeting date is created with a date later than the goal deadline
    Date plannedDate = meeting.getDate();
    Date goalDeadline = this.getGoal(projectId, goalId).getDeadline();
    if (plannedDate.after(goalDeadline)) {
      throw new IllegalStateException("The meeting date cannot be later than the goal deadline!");
    }
    // make sure these fields are set, because they are not specified in the body of the POST request
    meeting.setMeetingId(max + 1);
    meeting.setGoalId(goalId);
    meeting.setProjectId(projectId);
    meetingRepository.save(meeting);

    // send created meeting via MQ to the joinLink_service
    jmsTemplate.convertAndSend(meetingQueue, meeting);
  }

  @Transactional
  public void deleteMeeting(Long projectId, Long goalId, Long meetingId) {
    // throw an exception if the goal of the meeting does not exist
    try {
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    Meeting meeting = meetingRepository.findMeetingByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId).orElseThrow(() -> new IllegalStateException(
        "Meeting with Id " + meetingId + " does not exist within this goal within this project."
    ));
    // only planned meetings can be deleted (cancelled). Meetings that already happened should persist
    Date currentDate = new Date();
    if (this.getMeeting(projectId, goalId, meetingId).get().getDate().after(currentDate)) {
      // delete all tasks discussed at this meeting
      discussedRepository.deleteAllByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId);
      // delete the meeting itself
      meetingRepository.deleteMeetingByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId);
    } else {
      throw new IllegalStateException("You cannot delete a meeting that has already happened! " +
          "You can only delete meetings that are planned but haven't happened yet");
    }
  }

  @Transactional
  public void rescheduleMeeting(Long projectId, Long goalId, Long meetingId, Date date) {
    Meeting meeting = meetingRepository.findMeetingByProjectIdAndGoalIdAndMeetingId(
        projectId, goalId, meetingId).orElseThrow(() -> new IllegalStateException(
        "Meeting with id " + meetingId + " does not exist within this goal within this project"
    ));
    // throw an exception if the goal of the task does not exist
    try {
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }

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
        projectId, goalId, meetingId).orElseThrow(() -> new IllegalStateException(
        "Meeting with id " + meetingId + " does not exist within this goal within this project"
    ));
    // throw an exception if the goal of the meeting does not exist
    try {
      Goal goal = this.getGoal(projectId, goalId);
    } catch (IllegalStateException e) {
      throw e;
    }
    if (joinLink.equals(null)) {
      throw new IllegalStateException("JoinLink cannot be null");
    } else {
      meeting.setJoinLink(joinLink);
    }
  }

  /*
  The following operations are about tasks DISCUSSED at a meeting
   */

  public List<Long> getTasksOfMeeting(Long projectId, Long goalId, Long meetingId) {
    // throw an exception if the meeting of the meetingTasks does not exist
    try {
      Optional<Meeting> meeting = this.getMeeting(projectId, goalId, meetingId);
    } catch (IllegalStateException e) {
      throw e;
    }
    List<Discussed> meetingTasks = discussedRepository.findAllByProjectIdAndGoalIdAndMeetingId(projectId, goalId, meetingId);
    List<Long> meetingTaskIds = new ArrayList<>();
    for(Discussed meetingTask : meetingTasks) {
      meetingTaskIds.add(meetingTask.getTaskId());
    }
    return meetingTaskIds;
  }

  public void addTaskToMeeting(Long projectId, Long goalId, Long meetingId, Long taskId) {
    // throw an exception if the meeting does not exist
    try {
      Optional<Meeting> meeting = this.getMeeting(projectId, goalId, meetingId);
    } catch (IllegalStateException e) {
      throw e;
    }
    // throw an exception if the task does not exist
    try {
      Task task = this.getTask(projectId, goalId, taskId);
    } catch (IllegalStateException e) {
      throw e;
    }
    Meeting meeting = this.getMeeting(projectId, goalId, meetingId).get();
    Date currentDate = new Date();
    if (meeting.getDate().after(currentDate)) {
      Discussed meetingTask = new Discussed(meetingId, taskId, projectId, goalId, null);
      discussedRepository.save(meetingTask);
    } else {
      throw new IllegalStateException("No tasks can be added to a meeting that has already happened");
    }
  }

  @Transactional
  public void deleteTaskFromMeeting(Long projectId, Long goalId, Long meetingId, Long taskId) {
    // throw an exception if the meeting of the meetingTask does not exist
    try {
      Optional<Meeting> meeting = this.getMeeting(projectId, goalId, meetingId);
    } catch (IllegalStateException e) {
      throw e;
    }
    Discussed meetingTask = discussedRepository.findDiscussedByProjectIdAndGoalIdAndMeetingIdAndTaskId(projectId, goalId, meetingId, taskId).orElseThrow(() -> new IllegalStateException(
        "Task with Id " + taskId + " does not exist within this meeting"
    ));
    discussedRepository.deleteDiscussedByProjectIdAndGoalIdAndMeetingIdAndTaskId(projectId, goalId, meetingId, taskId);
  }

  // Get information from project_service
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  public Goal getGoal(Long projectId, Long goalId) {
    String url = projectService + "api/projects/" + projectId + "/goals/" + goalId;
    RestTemplate restTemplate = restTemplateBuilder.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
    try {
      Goal goal = restTemplate.getForObject(url, Goal.class);
      return goal;
    } catch (IllegalStateException e) {
      throw e;
    }
  }

  @Autowired
  private RestTemplateBuilder restTemplateBuilder2;

  public Task getTask(Long projectId, Long goalId, Long taskId) {
    String url = projectService + "api/projects/" + projectId + "/goals/" + goalId + "/tasks/" + taskId;
    RestTemplate restTemplate = restTemplateBuilder2.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
    try {
      Task task = restTemplate.getForObject(url, Task.class);
      return task;
    } catch (IllegalStateException e) {
      throw e;
    }
  }
}
