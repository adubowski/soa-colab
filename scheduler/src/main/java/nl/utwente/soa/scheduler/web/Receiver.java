package nl.utwente.soa.scheduler.web;

import nl.utwente.soa.scheduler.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.stereotype.Component;


@Component
public class Receiver {

//  @Autowired
//  private DefaultJmsListenerContainerFactory jmsListenerContainerFactory;
//  @Value("${ActiveMQ.queue.test}")
//  private String testQueue;

  @JmsListener(destination = "${ActiveMQ.queue.test}") // apparently @Value doesn't work here
  public void receiveMessage(Task task) {
    System.out.println("Received Task: " + task + " with ID " + task.getId() + " and name " + task.getName());
  }
}
