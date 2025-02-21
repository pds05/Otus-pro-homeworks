package ru.otus.java.pro.homeworks.jms.producer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ObjectMessage;
import jakarta.jms.TextMessage;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.homeworks.jms.dto.MessageDto;
import ru.otus.java.pro.homeworks.jms.producer.config.ActiveMqConfig;

import java.util.UUID;

@Slf4j
@Service
public class ActiveMqProducer {
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    public ActiveMqProducer(@Qualifier(ActiveMqConfig.JMS_TEMPLATE) JmsTemplate jmsTemplate,
                            ObjectMapper objectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(Object message, MessageType messageType) {
        switch (messageType) {
            case TEXT_MESSAGE -> jmsTemplate.send(createTextMessageCreator(message));
            case OBJECT_MESSAGE -> jmsTemplate.send(createObjectMessageCreator(message));
            default -> jmsTemplate.convertAndSend(message);
        }
        log.debug("Message sent to broker: '{}', messageType={}", message, messageType);
    }

    public void sendMessage(Object message) {
        sendMessage(message, MessageType.DEFAULT);
    }

    public void sendMessage(String message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setText(message);
        messageDto.setUuid(UUID.randomUUID());
        sendMessage(message, MessageType.DEFAULT);
    }

    public void sendMessage(String message, MessageType messageType) {
        MessageDto messageDto = new MessageDto();
        messageDto.setText(message);
        messageDto.setUuid(UUID.randomUUID());
        sendMessage(messageDto, messageType);
    }

    private MessageCreator createTextMessageCreator(Object message) {
        return session -> {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(convertToJson(message));
            textMessage.setStringProperty(ActiveMqConfig.CLASS_NAME, message.getClass().getName());
            return textMessage;
        };
    }

    private MessageCreator createObjectMessageCreator(Object message) {
        return session -> {
            ObjectMessage objectMessage = session.createObjectMessage();
            objectMessage.setObject(convertToJson(message));
            objectMessage.setStringProperty(ActiveMqConfig.CLASS_NAME, message.getClass().getName());
            return objectMessage;
        };
    }

    @SneakyThrows
    private String convertToJson(Object source) {
        return objectMapper.writeValueAsString(source);
    }

    @Getter
    public enum MessageType {
        DEFAULT(0), TEXT_MESSAGE(1), OBJECT_MESSAGE(2);

        private final int type;

        MessageType(int type) {
            this.type = type;
        }

        public static MessageType getMessageType(int type) {
            return switch (type) {
                case 1 -> MessageType.TEXT_MESSAGE;
                case 2 -> MessageType.OBJECT_MESSAGE;
                default -> MessageType.DEFAULT;
            };
        }
    }
}
