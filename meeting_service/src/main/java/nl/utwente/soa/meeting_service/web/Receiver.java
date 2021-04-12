package nl.utwente.soa.meeting_service.web;

import nl.utwente.soa.meeting_service.model.JoinLink;
import nl.utwente.soa.meeting_service.model.Meeting;
import nl.utwente.soa.meeting_service.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class Receiver {

  private final MeetingService meetingService;

  @Autowired
  public Receiver(MeetingService meetingService) {
    this.meetingService = meetingService;
  }

  @JmsListener(destination = "${ActiveMQ.queue.joinLink}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
  public void receiveMessage(JoinLink joinLink) {
    System.out.println(joinLink);
    meetingService.addJoinLinkToMeeting(joinLink.getProjectId(), joinLink.getGoalId(), joinLink.getMeetingId(), joinLink.getUrl());
  }
}
