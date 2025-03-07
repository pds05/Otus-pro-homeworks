package ru.otus.java.pro.mt.core.transfers.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.mt.core.transfers.dtos.RemainingLimitDto;
import ru.otus.java.pro.mt.core.transfers.integrations.limits.LimitsIntegration;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LimitsServiceImpl implements LimitsService {
    private final LimitsIntegration limitsIntegration;

    public boolean isLimitEnough(String clientId, String accountId, BigDecimal amount) {
        RemainingLimitDto limit = limitsIntegration.getRemainingLimit(clientId, accountId);
        return limit.getRemainingLimit().compareTo(amount) >= 0;
    }
}
