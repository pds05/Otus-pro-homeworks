package ru.otus.java.pro.mt.core.transfers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Перевод клиента", allOf = ExecuteTransferDtoRq.class)
@NoArgsConstructor
@Data
@AllArgsConstructor
public class TransferDto {
    @Schema(
            description = "Идентификатор перевода",
            example = "bde76ffa-f133-4c23-9bca-03618b2a94b2",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 36,
            maxLength = 36
    )
    private String id;

    @Schema(
            description = "Идентификатор клиента отправителя",
            example = "1234567890",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 10
    )
    private String clientId;
    private String targetClientId;
    private String sourceAccount;
    private String targetAccount;
    private String message;
    private BigDecimal amount;
}