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
import ru.otus.java.pro.mt.core.transfers.configs.properties.StatisticsIntegrationProperties;

@Configuration
public class RestClientsConfig {
    private static final String BASE_REST_CLIENT_FACTORY = "baseRestClientFactory";
    private static final String LIMITS_REST_CLIENT_FACTORY = "limitsRestClientFactory";
    private static final String STAT_REST_CLIENT_FACTORY = "statRestClientFactory";
    public static final String BASE_REST_CLIENT = "baseRestClient";
    public static final String LIMITS_REST_CLIENT = "limitsRestClient";
    public static final String STATISTICS_REST_CLIENT = "statRestClient";

    // @Bean
    public RestTemplate commonRestTemplate() {
        return new RestTemplate();
    }

    @Bean(STAT_REST_CLIENT_FACTORY)
    public ClientHttpRequestFactory statisticsHttpRequestFactory(StatisticsIntegrationProperties properties) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        return factory;
    }

    @Bean(STATISTICS_REST_CLIENT)
    public RestClient statisticsClient(@Qualifier(STAT_REST_CLIENT_FACTORY) ClientHttpRequestFactory requestFactory,
                                       StatisticsIntegrationProperties properties) {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(properties.getUrl())
                .build();
    }

    @Bean(LIMITS_REST_CLIENT_FACTORY)
    public ClientHttpRequestFactory limitsHttpRequestFactory(LimitsIntegrationProperties properties) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        return factory;
    }

    @Bean(LIMITS_REST_CLIENT)
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestClient limitsClient(@Qualifier(LIMITS_REST_CLIENT_FACTORY) ClientHttpRequestFactory requestFactory,
                                   LimitsIntegrationProperties properties) {
        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(properties.getUrl())
//                .defaultUriVariables(Map.of("variable", "foo"))
//                .defaultHeader("My-Header", "Foo")
//                .requestInterceptor(myCustomInterceptor)
//                .requestInitializer(myCustomInitializer)
                .build();
    }

    @Bean(BASE_REST_CLIENT_FACTORY)
    @ConditionalOnMissingBean(RestTemplate.class)
    public ClientHttpRequestFactory baseHttpRequestFactory(RestClientProperties properties) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        return factory;
    }

    @Bean(BASE_REST_CLIENT)
    @ConditionalOnBean(name = BASE_REST_CLIENT_FACTORY)
    public RestClient restClient(@Qualifier(BASE_REST_CLIENT_FACTORY) ClientHttpRequestFactory requestFactory,
                                 RestClientProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.getUrl())
                .requestFactory(requestFactory)
                .build();
    }
}
