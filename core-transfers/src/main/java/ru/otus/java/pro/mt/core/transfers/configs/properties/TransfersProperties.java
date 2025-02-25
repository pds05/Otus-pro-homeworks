package ru.otus.java.pro.mt.core.transfers.configs.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "transfers")
@Data
public class TransfersProperties {
    private BigDecimal maxTransferSum;
    private boolean maxTransfersEnabled;
    private Set<String> blockedAccountNumbers;

    @Value("${spring.kafka.producer.topics.transfer-topic}")
    private String kafkaTopicName;
}
