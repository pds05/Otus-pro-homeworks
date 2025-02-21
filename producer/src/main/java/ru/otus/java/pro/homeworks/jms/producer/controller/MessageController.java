package ru.otus.java.pro.homeworks.jms.producer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.pro.homeworks.jms.producer.service.ActiveMqProducer;

@Slf4j
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final ActiveMqProducer activeMqProducer;

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping
    public void transferMessage(@RequestHeader(name = "message-type", defaultValue = "default") String messageTypeParam,
                                @RequestHeader(value = "message-type-id", required = false) Integer messageTypeIdParam,
                                @RequestBody String messageParam) {
        log.debug("Message received for async processing: '{}'", messageParam);
        ActiveMqProducer.MessageType messageType = messageTypeIdParam != null ?
                ActiveMqProducer.MessageType.getMessageType(messageTypeIdParam) :
                ActiveMqProducer.MessageType.valueOf(messageTypeParam.toUpperCase());
        activeMqProducer.sendMessage(messageParam, messageType);
    }
}
