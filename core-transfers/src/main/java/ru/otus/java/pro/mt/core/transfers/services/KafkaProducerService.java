package ru.otus.java.pro.mt.core.transfers.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService implements ProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topic, Object message) {
        kafkaTemplate.send(topic, message);
        log.debug("Message sent to Kafka broker, topic={}, message={}", topic, message);
    }
}
