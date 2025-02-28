package ru.otus.java.pro.mt.core.transfers.integrations.limits;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.otus.java.pro.mt.core.transfers.configs.properties.LimitsIntegrationProperties;
import ru.otus.java.pro.mt.core.transfers.dtos.RemainingLimitDto;
import ru.otus.java.pro.mt.core.transfers.exceptions_handling.BusinessLogicException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(RestTemplate.class)
public class LimitsIntegrationRestTemplateImpl implements LimitsIntegration {
    private final RestTemplate commonRestTemplate;
    private final LimitsIntegrationProperties limitsIntegrationProperties;

    public RemainingLimitDto getRemainingLimit(String clientId) {
        try {
//            RemainingLimitDto remainingLimit = commonRestTemplate
//                    .getForObject(limitsIntegrationProperties.getUrl(), RemainingLimitDto.class);
            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.put("client-id", List.of("1"));
            RequestEntity<Void> re = new RequestEntity<>(headers,HttpMethod.GET, new URI(limitsIntegrationProperties.getUrl()));
            RemainingLimitDto remainingLimit = commonRestTemplate.exchange(re, RemainingLimitDto.class).getBody();
            return remainingLimit;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new BusinessLogicException("CLIENT_LIMIT_DOES_NOT_EXIST", "Клиент не найден в сервисе лимитов");
            }
            throw e;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
