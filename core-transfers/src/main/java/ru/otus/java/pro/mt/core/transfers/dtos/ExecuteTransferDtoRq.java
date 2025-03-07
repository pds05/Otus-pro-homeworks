package ru.otus.java.pro.mt.core.transfers.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(description = "Запрос на исполнение перевода")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ExecuteTransferDtoRq {
    private String targetClientId;
    private String sourceAccount;
    private String targetAccount;
    private String message;
    private BigDecimal amount;
}