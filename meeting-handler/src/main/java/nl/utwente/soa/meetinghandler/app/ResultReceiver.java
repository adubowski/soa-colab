package nl.utwente.soa.meetinghandler.app;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ResultReceiver {

    @JmsListener(destination = "${queue.results}")
    public void receiveMessage() {

    }

    @JmsListener(destination = "${queue.error}")
    public void receiveErrorMessage() {

    }

}
