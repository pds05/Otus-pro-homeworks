package ru.otus.java.pro.mt.core.transfers.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import ru.otus.java.pro.mt.core.transfers.configs.properties.LimitsIntegrationProperties;
import ru.otus.java.pro.mt.core.transfers.configs.properties.RestClientProperties;

@Configuration
public class RestClientsConfig {
    public static final String LIMITS_REST_CLIENT = "limitsClient";
    public static final String BASE_REST_CLIENT = "restClient";

    private static final String REST_CLIENT_FACTORY = "restClientFactory";

    // @Bean
    public RestTemplate commonRestTemplate() {
        return new RestTemplate();
    }

    @Bean(LIMITS_REST_CLIENT)
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestClient limitsClient(LimitsIntegrationProperties properties) {
        return RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl(properties.getUrl())
//                .defaultUriVariables(Map.of("variable", "foo"))
//                .defaultHeader("My-Header", "Foo")
//                .requestInterceptor(myCustomInterceptor)
//                .requestInitializer(myCustomInitializer)
                .build();
    }

    @Bean(REST_CLIENT_FACTORY)
    @ConditionalOnMissingBean(RestTemplate.class)
    public ClientHttpRequestFactory customHttpRequestFactory(RestClientProperties properties) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        return factory;
    }

    @Bean(BASE_REST_CLIENT)
    @ConditionalOnBean(name = REST_CLIENT_FACTORY)
    public RestClient restClient(@Qualifier(REST_CLIENT_FACTORY) ClientHttpRequestFactory requestFactory,
                                 RestClientProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getUrl())
                .requestFactory(requestFactory)
                .build();
    }
}
