package ru.otus.java.pro.mt.limits.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.pro.mt.limits.dtos.RemainingLimitDto;

import java.math.BigDecimal;

@Tag(name = "Лимиты", description = "Методы проверки остатка на счете")
@RestController
@RequestMapping("/api/v1/limits")
public class LimitsController {
    @Operation(summary = "Запрос остатка на счете",
            responses = {
                    @ApiResponse(description = "Успешный ответ", responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = RemainingLimitDto.class))
                    )
            })
    @GetMapping("/check")
    public RemainingLimitDto checkLimit(
            @Parameter(description = "Идентификатор клиента",
                    required = true,
                    schema = @Schema(type = "string", maxLength = 10,
                            example = "1234567890"))
            @RequestHeader(name = "client-id") String clientId
    ) {
        return new RemainingLimitDto(clientId, BigDecimal.valueOf(1000));
    }
}
