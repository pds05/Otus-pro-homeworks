package ru.otus.java.pro.mt.core.transfers.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.mt.core.transfers.integrations.limits.LimitsIntegration;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LimitsServiceImpl {
    private final LimitsIntegration limitsIntegration;

    public boolean isLimitEnough(String clientId, BigDecimal amount) {
        BigDecimal remainingLimitAmount = limitsIntegration.getRemainingLimit(clientId).getRemainingLimit();
        return amount.compareTo(remainingLimitAmount) >= 0;
    }

    public BigDecimal getRemainingLimit(String clientId) {
        return limitsIntegration.getRemainingLimit(clientId).getRemainingLimit();
    }
}
