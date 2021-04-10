package nl.utwente.soa.meeting_service.web;

import java.util.ArrayList;
import java.util.List;
import nl.utwente.soa.meeting_service.model.Goal;
import nl.utwente.soa.meeting_service.model.Meeting;
import nl.utwente.soa.meeting_service.model.Task;
import nl.utwente.soa.meeting_service.services.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class Receiver {

//  @Autowired
//  private DefaultJmsListenerContainerFactory jmsListenerContainerFactory;
//  @Value("${ActiveMQ.queue.test}")
//  private String testQueue;
  private final SchedulerService schedulerService;

  @Autowired
  public Receiver(SchedulerService schedulerService) {
    this.schedulerService = schedulerService;
  }

  @JmsListener(destination = "${ActiveMQ.queue.task}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
  public void receiveMessage(Task task) {
    System.out.println("Received Task: " + task + " with ID " + task.getId() + " and name " + task.getName());

    // either the goal just arrived at the scheduler and is still unplanned
    Boolean alreadyPlanned = true;
    List<Meeting> unplannedMeetings = schedulerService.getUnplannedMeetings();
    for (Meeting unplannedMeeting : unplannedMeetings) {
      if (unplannedMeeting.getGoal().getGoalId().equals(task.getGoalId()) &&
          unplannedMeeting.getGoal().getProjectId().equals(task.getProjectId())) {
        schedulerService.addTask(task);
        alreadyPlanned = false;
      }
    }

    // or the goal is already planned as a meeting and is stored in the DB under the meeting handler
    if (alreadyPlanned) {
      // TODO
      System.out.println("Note that the goal to which this task belongs is already used before to create a meeting!");
    }


  }

  @JmsListener(destination = "${ActiveMQ.queue.goal}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
  public void receiveMessage(Goal goal) {
    System.out.println("Received Goal: " + goal + " with ID " + goal.getId() + " and name " + goal.getName());

    // a meeting id will be created in the schedulerService
    schedulerService.createUnplannedMeeting(new Meeting(null, false, false, null, goal, new ArrayList<Task>()));
  }
}
