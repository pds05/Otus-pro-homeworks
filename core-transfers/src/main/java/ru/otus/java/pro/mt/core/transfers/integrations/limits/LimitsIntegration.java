package ru.otus.java.pro.mt.core.transfers.integrations.limits;

import ru.otus.java.pro.mt.core.transfers.dtos.RemainingLimitDto;

public interface LimitsIntegration {
    RemainingLimitDto getRemainingLimit(String clientId);
}
