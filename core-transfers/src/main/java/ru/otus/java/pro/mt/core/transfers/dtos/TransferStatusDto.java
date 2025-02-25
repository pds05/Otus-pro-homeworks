package ru.otus.java.pro.mt.core.transfers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusDto {
    private String transferId;
    private TransferStatus status;

    public enum TransferStatus {
        EXECUTED, LIMIT_NOT_ENOUGH
    }
}
