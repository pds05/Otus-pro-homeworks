package ru.otus.java.pro.mt.limits.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Остаток клиента")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemainingLimitDto {
    @Schema(
            description = "Идентификатор клиента",
            example = "1234567890",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 10
    )
    private String clientId;
    @Schema(
            description = "Остаток на счете",
            example = "100.00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @Min(0)
    private BigDecimal remainingLimit;
}
