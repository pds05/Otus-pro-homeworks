package ru.otus.java.pro.mt.limits.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.pro.mt.limits.dtos.RemainingLimitDto;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/limits")
public class LimitsController {
    @GetMapping("/check/{clientId}")
    public ResponseEntity<RemainingLimitDto> checkLimit(
            @PathVariable String clientId,
//            @RequestHeader(name = "client-id") String clientId,
            @RequestParam(name = "account-id") String accountId
    ) {
        return (clientId.isBlank() || accountId.isBlank()) ?
                new ResponseEntity<>(new RemainingLimitDto(BigDecimal.ZERO), HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(new RemainingLimitDto(BigDecimal.valueOf(100)), HttpStatus.OK);
    }
}
