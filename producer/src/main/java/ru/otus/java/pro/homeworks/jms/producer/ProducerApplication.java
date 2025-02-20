package ru.otus.java.pro.homeworks.jms.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import ru.otus.java.pro.homeworks.jms.dto.MessageDto;
import ru.otus.java.pro.homeworks.jms.producer.service.ActiveMqProducer;

import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@EnableScheduling
@SpringBootApplication
public class ProducerApplication {

    private final ActiveMqProducer activeMqProducer;
    private final Random random = new Random();
    private int counter;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {
        MessageDto message = new MessageDto();
        message.setUuid(UUID.randomUUID());
        message.setText("Message number: " + ++counter);
        activeMqProducer.sendMessage(message,
                ActiveMqProducer.MessageType.getMessageType(random.nextInt(3)));
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

}
