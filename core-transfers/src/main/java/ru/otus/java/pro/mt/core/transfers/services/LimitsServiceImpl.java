package ru.otus.java.pro.mt.core.transfers.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.mt.core.transfers.integrations.limits.LimitsIntegration;

@Service
@RequiredArgsConstructor
public class LimitsServiceImpl {
    private final LimitsIntegration limitsIntegration;

    public boolean isLimitEnough() {
        return true;
    }
}
