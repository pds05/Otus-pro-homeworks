package ru.otus.java.pro.mt.core.transfers.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.mt.core.transfers.integrations.statisics.StatisticsIntegration;

@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService{
    private final StatisticsIntegration statisticsIntegration;

    @Override
    public void send(String message) {
        statisticsIntegration.send(message);
    }
}
