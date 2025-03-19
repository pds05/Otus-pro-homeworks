package ru.otus.java.pro.mt.core.transfers.configs.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "integrations.limits")
public class LimitsIntegrationProperties {
    private String url;
    private Duration readTimeout;
    private Duration connectTimeout;
}
