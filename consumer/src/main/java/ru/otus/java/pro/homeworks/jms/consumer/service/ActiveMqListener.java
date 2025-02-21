package ru.otus.java.pro.homeworks.jms.consumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.homeworks.jms.consumer.config.ActiveMqConfig;

@Slf4j
@Service
public class ActiveMqListener {
    private final MessageConverter messageConverter;
    private final ObjectMapper objectMapper;

    public ActiveMqListener(@Qualifier(ActiveMqConfig.MESSAGE_CONVERTER) MessageConverter messageConverter,
                            ObjectMapper objectMapper) {
        this.messageConverter = messageConverter;
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @JmsListener(destination = "${spring.jms.template.default-destination}",
            containerFactory = ActiveMqConfig.LISTENER_FACTORY
    )
    public void onMessage(Message message) {
        Object receivedObject;
        try {
            if (message instanceof ObjectMessage objectMessage) {
                String json = objectMessage.getObject().toString();
                Class<?> cls = Class.forName(message.getStringProperty(ActiveMqConfig.CLASS_NAME));
                receivedObject = objectMapper.readValue(json, cls);
            } else if (message instanceof TextMessage) {
                receivedObject = messageConverter.fromMessage(message);
            } else {
                throw new IllegalArgumentException("Message type not supported: " + message.getClass().getName());
            }
            log.debug("Message received from broker: {}", receivedObject);
        } catch (Exception e) {
            log.error("Error deserializing message {}", message, e);
        }
    }
}
