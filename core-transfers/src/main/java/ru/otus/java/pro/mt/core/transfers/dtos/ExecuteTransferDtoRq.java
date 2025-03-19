package ru.otus.java.pro.mt.core.transfers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Запрос на исполнение перевода")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ExecuteTransferDtoRq {
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
    @Min(1)
    private BigDecimal amount;
}