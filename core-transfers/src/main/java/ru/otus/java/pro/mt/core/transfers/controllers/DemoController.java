package ru.otus.java.pro.mt.core.transfers.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.pro.mt.core.transfers.metrics.CustomMetricsService;

@RestController
@RequiredArgsConstructor
public class DemoController {
    private final CustomMetricsService customMetricsService;

    @GetMapping("/demo")
    public void hello() {
        customMetricsService.incrementCustomMetric();
        customMetricsService.changeCustomGauge();
    }
}
