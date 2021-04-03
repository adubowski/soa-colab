package nl.utwente.soa.meetinghandler.app;

import java.util.HashMap;
import java.util.Map;
import nl.utwente.soa.meetinghandler.app.MQModels.ScheduledMeeting;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;


@EnableJms
@Configuration
public class JmsConfiguration {

  @Value("${ActiveMQ.broker.url}")
  private String brokerUrl; // It is not "http://localhost:8161/";
  @Value("${ActiveMQ.broker.username}")
  private String brokerUsername;
  @Value("${ActiveMQ.broker.password}")
  private String brokerPassword;

  @Autowired
  public JmsConfiguration(ResourceLoader resourceLoader) {
  }

  // Connect JMS to an external ActiveMQ session
  @Bean
  public ActiveMQConnectionFactory activeMQConnectionFactory() {
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
    activeMQConnectionFactory.setBrokerURL(brokerUrl);
    if (brokerUsername != null && !brokerUsername.isEmpty() && brokerPassword != null && !brokerPassword.isEmpty()) {
      activeMQConnectionFactory.setUserName(brokerUsername);
      activeMQConnectionFactory.setPassword(brokerPassword);
    }
    return activeMQConnectionFactory;
  }

  @Bean
  public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("_type");
    Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
    typeIdMappings.put("meeting", ScheduledMeeting.class);
    converter.setTypeIdMappings(typeIdMappings);
    return converter;
  }

  // Create the JmsListernerFactory with the correct marshaller.
  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(DefaultJmsListenerContainerFactoryConfigurer configurer) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    configurer.configure(factory, activeMQConnectionFactory());
//    factory.setConnectionFactory(activeMQConnectionFactory());
//    factory.setMessageConverter(jacksonJmsMessageConverter()); // I CHANGED jaxbMarshaller to jacksonJmsMessageConverter
//    factory.setConcurrency("3-10"); // limit concurrent listener
//    factory.setErrorHandler((e) -> {
//      throw new IllegalStateException("An error occurred while processing the MQ message");
//    });
    return factory;
  }

}
