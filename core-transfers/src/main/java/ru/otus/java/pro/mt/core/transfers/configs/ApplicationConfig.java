package ru.otus.java.pro.mt.core.transfers.configs;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public GroupedOpenApi transfersApiV1() {
        final String[] packagesToScan = {"ru.otus.java.pro.mt.core.transfers.controllers"};
        return GroupedOpenApi
                .builder()
                .group("1. mt-core-transfers-v1")
                .packagesToScan(packagesToScan)
                .pathsToMatch("/api/v1/transfers/**")
                .addOpenApiCustomizer(
                        openApi -> openApi.info(
                                new Info()
                                        .title("Микросервис переводов")
                                        .description("OTUS - МС Переводов - Переводы монет")
                                        .version("1.0.0")
                        )
                )
                .build();
    }
}
