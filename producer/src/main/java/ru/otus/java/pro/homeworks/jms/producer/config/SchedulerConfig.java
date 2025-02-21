package ru.otus.java.pro.homeworks.jms.producer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.otus.java.pro.homeworks.jms.dto.MessageDto;
import ru.otus.java.pro.homeworks.jms.producer.service.ActiveMqProducer;

import java.util.Random;
import java.util.UUID;

@Slf4j
@ConditionalOnProperty(name = "scheduler.enable-send-message", havingValue = "true")
@EnableScheduling
@Configuration
public class SchedulerConfig {
    private final ActiveMqProducer activeMqProducer;
    private final Random random = new Random();
    private int counter;

    public SchedulerConfig(ActiveMqProducer activeMqProducer) {
        this.activeMqProducer = activeMqProducer;
        log.info("Scheduler started");
    }

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        MessageDto message = new MessageDto();
        message.setUuid(UUID.randomUUID());
        message.setText("Message number: " + ++counter);
        log.debug("Scheduler sending message {}", message);
        activeMqProducer.sendMessage(message,
                ActiveMqProducer.MessageType.getMessageType(random.nextInt(3)));
    }
}
