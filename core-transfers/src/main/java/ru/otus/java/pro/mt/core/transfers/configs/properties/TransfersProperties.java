package ru.otus.java.pro.mt.core.transfers.configs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.Set;

@ConfigurationProperties(prefix = "transfers")
@Data
public class TransfersProperties {
    private BigDecimal maxTransferSum;
    private boolean maxTransfersEnabled;
    private Set<String> blockedAccountNumbers;
    private PagingOffset pagingOffset;

    @Data
    public static class PagingOffset {
        private Integer defaultValue;
        private Integer maximumValue;
    }
}
