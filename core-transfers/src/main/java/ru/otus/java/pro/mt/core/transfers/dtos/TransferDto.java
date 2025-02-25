package ru.otus.java.pro.mt.core.transfers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Перевод клиента")
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

    @Schema(
            description = "Идентификатор клиента получателя",
            example = "1234567890",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 10
    )
    private String targetClientId;

    @Schema(
            description = "Номер счета отправителя",
            example = "123456789012",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 12,
            maxLength = 12
    )
    private String sourceAccount;

    @Schema(
            description = "Номер счета получателя",
            example = "123456789012",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 12,
            maxLength = 12
    )
    private String targetAccount;

    @Schema(
            description = "Сообщение получателю",
            example = "На день рождения",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 255
    )
    private String message;

    @Schema(
            description = "Сумма",
            example = "100.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private BigDecimal amount;
}