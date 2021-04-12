package nl.utwente.soa.join_link_service.services;

import nl.utwente.soa.join_link_service.model.JoinLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JoinLinkService {

  private final JmsTemplate jmsTemplate;
  @Value("${ActiveMQ.queue.joinLink}")
  private String joinLinkQueue;

  @Autowired
  public JoinLinkService(JmsTemplate jmsTemplate){
    this.jmsTemplate = jmsTemplate;
  }

  public void generateJoinLink(Long projectId, Long goalId, Long meetingId) {
    JoinLink joinLink = new JoinLink(projectId, goalId, meetingId, "dummy.join.link.meeting" + meetingId);
    jmsTemplate.convertAndSend(joinLinkQueue, joinLink);
  }
}
