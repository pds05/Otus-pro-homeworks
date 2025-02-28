package ru.otus.java.pro.mt.core.transfers.integrations.limits;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.otus.java.pro.mt.core.transfers.dtos.RemainingLimitDto;
import ru.otus.java.pro.mt.core.transfers.exceptions_handling.BusinessLogicException;

@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(RestTemplate.class)
public class LimitsIntegrationRestClientImpl implements LimitsIntegration {
    private final RestClient limitsClient;

    public RemainingLimitDto getRemainingLimit(String clientId) {
        return limitsClient
                .get()
                .uri("/check")
                .header("client-id", clientId)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.value() == HttpStatus.NOT_FOUND.value(), (request, response) -> {
                    throw new BusinessLogicException("CLIENT_LIMIT_DOES_NOT_EXIST", "Клиент не найден в сервисе лимитов");
                })
                .body(RemainingLimitDto.class);
    }
}
