package ru.otus.java.pro.mt.core.transfers.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TransferMetricsService {
    private final Counter totalTransferCounter;
    private final Counter successTransferCounter;
    private final Counter failedTransferCounter;

    public TransferMetricsService(MeterRegistry meterRegistry) {
        this.totalTransferCounter = Counter.builder("total_transfers")
                .description("Total number of transfers")
                .tags("environment", "development")
                .register(meterRegistry);
        this.successTransferCounter = Counter.builder("success_transfers")
                .description("Number of successful transfers")
                .tags("environment", "development")
                .register(meterRegistry);
        this.failedTransferCounter = Counter.builder("failed_transfers")
                .description("Number of failed transfers")
                .tags("environment", "development")
                .register(meterRegistry);
    }

    public void incrementTotalTransfersCounter() {
        totalTransferCounter.increment();
    }

    public void incrementSuccessTransfersCounter() {
        successTransferCounter.increment();
    }

    public void incrementFailedTransfersCounter() {
        failedTransferCounter.increment();
    }
}
