package nl.utwente.soa.meeting_service.web;

import nl.utwente.soa.meeting_service.services.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class Receiver {

  private final MeetingService meetingService;

  @Autowired
  public Receiver(MeetingService meetingService) {
    this.meetingService = meetingService;
  }

  // TODO: Implement a JMSListener Receive method that listnes to JoinLinkQueue
  @JmsListener(destination = "${ActiveMQ.queue.joinlink}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
  public void receiveMessage(String url) {

  }
}
