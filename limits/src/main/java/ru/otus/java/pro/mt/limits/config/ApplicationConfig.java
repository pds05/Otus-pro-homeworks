package ru.otus.java.pro.mt.limits.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    final String[] packagesToScan = {"ru.otus.java.pro.mt.limits.controllers"};

    @Bean
    public GroupedOpenApi limitsApiV1() {
        return GroupedOpenApi.builder()
                .group("1. mt-limits-v1")
                .packagesToScan(packagesToScan)
                .pathsToMatch("/api/v1/limits/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("Микросервис проверки клиентского баланса")
                        .description("OTUS - МС Лимитов - Остаток на счете")
                        .version("1.0.0")))
                .build();
    }
}
