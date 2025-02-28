package ru.otus.java.pro.mt.core.transfers.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TransfersPageDto(
        @JsonProperty(JSON_WRAPPER_NAME)
        @ArraySchema(schema = @Schema(description = "Переводы клиента",
                implementation = TransferDto.class, name = JSON_WRAPPER_NAME))
        List<TransferDto> entries) {
        public static final String JSON_WRAPPER_NAME = "transfers";
}