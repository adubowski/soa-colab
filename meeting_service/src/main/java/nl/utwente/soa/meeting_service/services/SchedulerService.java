package nl.utwente.soa.meeting_service.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import nl.utwente.soa.meeting_service.model.Meeting;
import nl.utwente.soa.meeting_service.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SchedulerService {
  private static List<Meeting> unplannedMeetings = new ArrayList<Meeting>();

  private final JmsTemplate jmsTemplate;
  @Value("${ActiveMQ.queue.meeting}")
  private String meetingQueue;
  @Value("${service.meeting_handler}")
  private String meetingHandlerService;

  @Autowired
  public SchedulerService(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  public List<Meeting> getUnplannedMeetings() {
    return unplannedMeetings;
  }

  public Meeting getUnplannedMeeting(Long meetingId){
    return unplannedMeetings.stream()
        .filter(unplannedMeeting -> meetingId.equals(unplannedMeeting.getMeetingId()))
        .findAny()
        .orElse(null);
  }

  public void createMeetingFromUnplannedMeeting(Long meetingId, Date date) {
    Meeting unplannedMeeting = unplannedMeetings.stream()
        .filter(meeting -> meetingId.equals(meeting.getMeetingId()))
        .findAny()
        .orElse(null);

    Meeting plannedMeeting = unplannedMeeting;
    plannedMeeting.setDate(date);
    plannedMeeting.setPlanned(true);

    // send planned meeting via MQ to the meeting-handler
    jmsTemplate.convertAndSend(meetingQueue, plannedMeeting);

    // finally remove the meeting from the list of unplannedMeetings
    unplannedMeetings.removeIf(meeting -> meeting.getMeetingId().equals(meetingId));
  }

  // when a task arrives from the task-MQ, this method is called
  public void addTask(Task task) {
    for (Meeting unplannedMeeting : unplannedMeetings) {
      if (unplannedMeeting.getGoal().getGoalId().equals(task.getGoalId()) &&
          unplannedMeeting.getGoal().getProjectId().equals(task.getProjectId())) {
        List<Task> tasks = unplannedMeeting.getTasks();
        tasks.add(task);
        unplannedMeeting.setTasks(tasks);
      }
    }
  }

  // when a goal arrives from the goal-MQ, this method is called
  public void createUnplannedMeeting(Meeting meeting) {
    // Make a synchronous request to Meeting Handler to create a new ID.
    List<Meeting> plannedMeetings = this.getMeetings();
    Long max = 0L;
    for (Meeting plannedMeeting : plannedMeetings) {
      if (plannedMeeting.getMeetingId() > max) {
        max = plannedMeeting.getMeetingId();
      }
    }
    meeting.setMeetingId(max + 1);
    unplannedMeetings.add(meeting);
  }

  // Get information from meeting handler
  @Autowired
  private RestTemplateBuilder restTemplateBuilder;

  public List<Meeting> getMeetings() {
    String url = meetingHandlerService + "/api/meetings";
    RestTemplate restTemplate = restTemplateBuilder.build(); //errorHandler(new RestTemplateResponseErrorHandler()).build();
    try {
      Meeting[] meetings = restTemplate.getForObject(url, Meeting[].class);
      return new ArrayList<>(Arrays.asList(meetings));
    } catch (IllegalStateException e) {
      throw e;
    }
  }
}
