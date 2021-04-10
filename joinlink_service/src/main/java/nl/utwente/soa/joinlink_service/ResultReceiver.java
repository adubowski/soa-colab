package nl.utwente.soa.joinlink_service;

import nl.utwente.soa.joinlink_service.MQModels.ScheduledMeeting;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ResultReceiver {

    @JmsListener(destination = "${ActiveMQ.queue.meeting}", containerFactory = "jmsListenerContainerFactory") // apparently @Value doesn't work here
    public void receiveMessage(ScheduledMeeting scheduledMeeting) {
        System.out.println("Received Scheduled Meeting from Scheduler: " + scheduledMeeting + " with ID " + scheduledMeeting.getMeetingId());
    }

}
