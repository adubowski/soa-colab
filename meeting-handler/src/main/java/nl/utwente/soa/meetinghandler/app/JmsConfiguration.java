//package nl.utwente.soa.meetinghandler.app;
//
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.slf4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.jms.annotation.EnableJms;
//import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
//import org.springframework.jms.support.converter.MarshallingMessageConverter;
//import org.springframework.jms.support.converter.MessageConverter;
//import org.springframework.jms.support.converter.MessageType;
//import org.springframework.oxm.jaxb.Jaxb2Marshaller;
//
//import java.util.Collections;
//
//
//@EnableJms
//@Configuration
//public class JmsConfiguration {
//
//    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JmsConfiguration.class);
//
//    @Value("${activemq.broker-url}")
//    private String brokerUrl;
//
//    @Value("${activemq.broker-username}")
//    private String brokerUsername;
//
//    @Value("${activemq.broker-password}")
//    private String brokerPassword;
//
//    private final ResourceLoader resourceLoader;
//
//    @Autowired
//    public JmsConfiguration(ResourceLoader resourceLoader) {
//        this.resourceLoader = resourceLoader;
//    }
//
//    /**
//     * Connect JMS to an external ActiveMQ session, based on the active.broker-url of application.properties
//     */
//    @Bean
//    public ActiveMQConnectionFactory activeMQConnectionFactory() {
//        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
//
//        activeMQConnectionFactory.setBrokerURL(brokerUrl);
//
//        log.info("Connect to ActiveMQ host: {}", brokerUrl);
//
//        if(brokerUsername != null && !brokerUsername.isEmpty() && brokerPassword != null && !brokerPassword.isEmpty()) {
//            activeMQConnectionFactory.setUserName(brokerUsername);
//            activeMQConnectionFactory.setPassword(brokerPassword);
//        }
//
//        return activeMQConnectionFactory;
//    }
//
//
// REPLACE WITH JSON CONVERTER
//    /**
//     * Setup using a XML (jaxb) message converter for marshalling and unmarshalling the messages to xml
//     */
//    @Bean
//    public MessageConverter jaxbMarshaller() {
//        // New XML Marshaller
//        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
//        jaxb2Marshaller.setPackagesToScan("nl.utwente.soa.integrations");
//        jaxb2Marshaller.setSchemas(
//            resourceLoader.getResource("classpath:xsd/CalculatorResponse.xsd"),
//            resourceLoader.getResource("classpath:xsd/CalculatorTask.xsd")
//        );
//        jaxb2Marshaller.setMarshallerProperties(Collections.singletonMap("jaxb.formatted.output", true));
//        try {
//            jaxb2Marshaller.afterPropertiesSet();
//        } catch (Exception e) {
//            log.error("Could not update marshaller properties", e);
//        }
//
//
//        // Configure it in the JMS message converter
//        MarshallingMessageConverter converter = new MarshallingMessageConverter();
//        converter.setTargetType(MessageType.TEXT);
//        converter.setMarshaller(jaxb2Marshaller);
//        converter.setUnmarshaller(jaxb2Marshaller);
//        return converter;
//    }
//
//
//    /**
//     * Create the JmsListernerFactory with the correct marshaller.
//     */
//    @Bean
//    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(activeMQConnectionFactory());
//        factory.setMessageConverter(jaxbMarshaller());
//        factory.setConcurrency("3-10"); // limit concurrent listener
//        factory.setErrorHandler((e) -> {
//            log.error("An error occured while processing the MQ message", e);
//        });
//
//        return factory;
//    }
//
//}
