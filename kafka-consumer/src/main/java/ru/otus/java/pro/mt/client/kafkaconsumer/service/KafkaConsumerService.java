package ru.otus.java.pro.mt.client.kafkaconsumer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.mt.client.kafkaconsumer.dtos.TransferStatusDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    @KafkaListener(
            topics = "${spring.kafka.topics.transfer-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=ru.otus.java.pro.mt.client.kafkaconsumer.dtos.TransferStatusDto"})
    public void readTransferStatus(TransferStatusDto transferStatusDto) {
        log.debug("Received message from Kafka topic {}", transferStatusDto);
        log.info("По переводу {} клиенту отправлена нотификация", transferStatusDto.transferId());
    }
}
