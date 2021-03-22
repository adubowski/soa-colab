package nl.utwente.soa.goalservice.web;

import java.util.Collections;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;


@EnableJms
@Configuration
public class JmsConfiguration {

  private String brokerUrl = "tcp://127.0.0.1:61616";//"http://localhost:8161/";
  private String brokerUsername;
  private String brokerPassword;

  private final ResourceLoader resourceLoader;

  @Autowired
  public JmsConfiguration(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  // Connect JMS to an external ActiveMQ session
  @Bean
  public ActiveMQConnectionFactory activeMQConnectionFactory() {
    ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
    activeMQConnectionFactory.setBrokerURL(brokerUrl);
    if(brokerUsername != null && !brokerUsername.isEmpty() && brokerPassword != null && !brokerPassword.isEmpty()) {
      activeMQConnectionFactory.setUserName(brokerUsername);
      activeMQConnectionFactory.setPassword(brokerPassword);
    }
    return activeMQConnectionFactory;
  }

  // Setup using a XML (jaxb) message converter for marshalling and unmarshalling the messages to xml
  @Bean
  public MessageConverter jaxbMarshaller() {
    // New XML Marshaller
    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setPackagesToScan("nl.utwente.soa.workdistributor.model");
//    jaxb2Marshaller.setSchemas(
//        // XML Schemas (XSD files) should be created in resources/xsd/
//        resourceLoader.getResource("classpath:xsd/CalculatorResponse.xsd"),
//        resourceLoader.getResource("classpath:xsd/CalculatorTask.xsd")
//    );
    jaxb2Marshaller.setMarshallerProperties(Collections.singletonMap("jaxb.formatted.output", true));
    try {
      jaxb2Marshaller.afterPropertiesSet();
    } catch (Exception e) {
      throw new IllegalStateException("Could not update marshaller properties");
    }
    // Configure it in the JMS messageconverter
    MarshallingMessageConverter converter = new MarshallingMessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setMarshaller(jaxb2Marshaller);
    converter.setUnmarshaller(jaxb2Marshaller);
    return converter;
  }

  // Create the JmsListernerFactory with the correct marshaller.
  @Bean
  public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    factory.setConnectionFactory(activeMQConnectionFactory());
    factory.setMessageConverter(jaxbMarshaller());
    factory.setConcurrency("3-10"); // limit concurrent listener
    factory.setErrorHandler((e) -> {
      throw new IllegalStateException("An error occured while processing the MQ message");
    });
    return factory;
  }

}
