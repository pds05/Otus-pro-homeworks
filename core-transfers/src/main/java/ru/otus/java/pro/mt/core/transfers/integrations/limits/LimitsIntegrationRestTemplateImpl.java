package ru.otus.java.pro.mt.core.transfers.integrations.limits;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.otus.java.pro.mt.core.transfers.configs.properties.LimitsIntegrationProperties;
import ru.otus.java.pro.mt.core.transfers.dtos.RemainingLimitDto;
import ru.otus.java.pro.mt.core.transfers.exceptions_handling.BusinessLogicException;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(RestTemplate.class)
public class LimitsIntegrationRestTemplateImpl implements LimitsIntegration {
    private final RestTemplate commonRestTemplate;
    private final LimitsIntegrationProperties limitsIntegrationProperties;

    public RemainingLimitDto getRemainingLimit(String clientId) {
        try {
            RemainingLimitDto remainingLimit = commonRestTemplate.getForObject(limitsIntegrationProperties.getUrl(), RemainingLimitDto.class);
            return remainingLimit;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new BusinessLogicException("CLIENT_LIMIT_DOES_NOT_EXIST", "Клиент не найден в сервисе лимитов");
            }
            throw e;
        }
    }
}
