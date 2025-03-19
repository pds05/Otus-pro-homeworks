package ru.otus.java.pro.mt.core.transfers.integrations.statisics;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.otus.java.pro.mt.core.transfers.configs.RestClientsConfig;

@Component
public class StatisticsIntegrationRestClientImpl implements StatisticsIntegration {
    private final RestClient restClient;

    public StatisticsIntegrationRestClientImpl(@Qualifier(RestClientsConfig.STATISTICS_REST_CLIENT) RestClient statisticsClient) {
        this.restClient = statisticsClient;
    }

    @Override
    public void send(String message) {
        restClient
                .post()
                .uri("/upload")
                .contentType(MediaType.TEXT_HTML)
                .body(message)
                .retrieve()
                .toBodilessEntity();
    }
}
