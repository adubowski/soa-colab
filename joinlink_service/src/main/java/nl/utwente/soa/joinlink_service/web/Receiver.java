package nl.utwente.soa.joinlink_service.web;

import nl.utwente.soa.joinlink_service.model.Meeting;
import nl.utwente.soa.joinlink_service.services.JoinLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    private final JoinLinkService joinLinkService;

    @Autowired
    public Receiver(JoinLinkService joinLinkService) {
        this.joinLinkService = joinLinkService;
    }

    @JmsListener(destination = "${ActiveMQ.queue.meeting}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
    public void receiveMessage(Meeting meeting) {
        System.out.println("Received Meeting from Meeting_service: " + meeting + " with ID " + meeting.getMeetingId());

        joinLinkService.generateJoinLink(meeting.getProjectId(), meeting.getGoalId(), meeting.getMeetingId());
    }

}
