package ru.otus.java.pro.mt.limits.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.java.pro.mt.limits.dtos.RemainingLimitDto;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/limits")
public class LimitsController {
    @GetMapping("/check")
    public RemainingLimitDto checkLimit(
            @RequestHeader(name = "client-id") String clientId
    ) {
        return new RemainingLimitDto(BigDecimal.valueOf(1000));
    }
}
