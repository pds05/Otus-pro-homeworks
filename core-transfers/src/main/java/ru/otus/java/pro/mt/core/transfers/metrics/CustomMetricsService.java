package ru.otus.java.pro.mt.core.transfers.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class CustomMetricsService {
    private final Counter customMetricCounter;
    private final AtomicInteger customMetricGauge;

    public CustomMetricsService(MeterRegistry meterRegistry) {
        customMetricCounter = Counter.builder("custom_metric_name")
                .description("Description of custom metric")
                .tags("environment", "development")
                .register(meterRegistry);

        customMetricGauge = meterRegistry.gauge("custom_gauge", new AtomicInteger(0));
    }

    public void incrementCustomMetric() {
        customMetricCounter.increment();
    }

    public void changeCustomGauge() {
        customMetricGauge.set((int)(Math.random() * 1000));
    }
}
