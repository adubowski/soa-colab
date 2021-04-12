package nl.utwente.soa.meeting_service.web;

import java.util.Date;
import java.util.List;
import nl.utwente.soa.meeting_service.model.Goal;
import nl.utwente.soa.meeting_service.model.JoinLink;
import nl.utwente.soa.meeting_service.model.Meeting;
import nl.utwente.soa.meeting_service.model.Task;
import nl.utwente.soa.meeting_service.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class Receiver {

//  @Autowired
//  private DefaultJmsListenerContainerFactory jmsListenerContainerFactory;
//  @Value("${ActiveMQ.queue.test}")
//  private String testQueue;
  private final MeetingService meetingService;

  @Autowired
  public Receiver(MeetingService meetingService) {
    this.meetingService = meetingService;
  }

  // TODO: Implement a JMSListener Receive method that listnes to JoinLinkQueue
  @JmsListener(destination = "${ActiveMQ.queue.joinlink}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
  public void receiveMessage(JoinLink joinLink) {

  }

//  @JmsListener(destination = "${ActiveMQ.queue.task}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
//  public void receiveMessage(Task task) {
//    System.out.println("Received Task: " + task + " with ID " + task.getId() + " and name " + task.getName());
//    /*
//     CASES
//     1: The task belongs to a goal for which no (unplanned) meeting is created yet
//        A new unplanned meeting with task.getGoalId() is created
//     2: The task belongs to a goal for which already an unplanned meeting is created
//        The task is added to the unplanned meeting with the same goal
//     3: The task belongs to a goal for which already a meeting is planned
//        The user is asked whether: 1. it wants to add the task to the planned meeting OR 2. create a new unplanned meeting with task.getGoalId()
//     4: The task belongs to a goal for which already the meeting has finished
//        A new unplanned meeting with task.getGoalId() is created
//     NOTE
//     unplanned --> date == null               planned --> date != null
//     unfinished --> date.now < date.planned   finished --> date.now >= date.planned
//     */
//
//    // if not CASE 2 or 3, then do CASE 1 or 4.
//    if (!meetingService.addTaskToMeeting(task)) {
//      meetingService.createNewMeeting(task.getProjectId(), task.getGoalId(), new Meeting());
//      if (!meetingService.addTaskToMeeting(task)) {
//        throw new IllegalStateException("The task somehow cannot be added to a meeting that was just created");
//      }
//    }
//
//  }

//  @JmsListener(destination = "${ActiveMQ.queue.goal}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
//  public void receiveMessage(Goal goal) {
//    System.out.println("Received Goal: " + goal + " with ID " + goal.getId() + " and name " + goal.getName());
//
//    // a meeting id will be created in the schedulerService
//    // TODO: maxMeetingId = getMaxMeetingId()
//    // TODO: newMeetingID = maxMeetingId + 1
//    meetingService.createUnplannedMeeting(new Meeting(null, false, null));
//  }
}
