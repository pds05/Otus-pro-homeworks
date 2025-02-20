package ru.otus.java.pro.homeworks.jms.consumer.config;

import jakarta.jms.ConnectionFactory;
import lombok.SneakyThrows;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import ru.otus.java.pro.homeworks.jms.dto.MessageDto;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class ActiveMqConfig {
    public static final String JMS_TEMPLATE = "activeMqTemplate";
    public static final String LISTENER_FACTORY = "activeMqListenerFactory";
    public static final String CONNECTION_FACTORY = "activeMqConnectionFactory";
    public static final String MESSAGE_CONVERTER = "messageConverter";
    public static final String DESTINATION_QUEUE = "message.queue";
    public static final String CLASS_NAME = "className";

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String userName;
    @Value("${spring.activemq.password}")
    private String password;

    @SneakyThrows
    @Bean(CONNECTION_FACTORY)
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        connectionFactory.setUserName(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }

    @Bean(name = LISTENER_FACTORY)
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(@Qualifier(CONNECTION_FACTORY) ConnectionFactory connectionFactory,
                                                                      @Qualifier(MESSAGE_CONVERTER) MessageConverter messageConverter,
                                                                      DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(messageConverter);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean(MESSAGE_CONVERTER)
    public MessageConverter jacksonMessageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTargetType(MessageType.TEXT);
        messageConverter.setTypeIdPropertyName(CLASS_NAME);

        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put(MessageDto.class.getSimpleName(), MessageDto.class);
        messageConverter.setTypeIdMappings(typeIdMappings);
        return messageConverter;
    }

    @Bean(JMS_TEMPLATE)
    public JmsTemplate jmsTemplate(@Qualifier(CONNECTION_FACTORY) ConnectionFactory connectionFactory,
                                   @Qualifier(MESSAGE_CONVERTER) MessageConverter messageConverter) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.setReceiveTimeout(TimeUnit.SECONDS.toMillis(10));
        return jmsTemplate;
    }
}
