package ru.otus.java.pro.spring.app.dtos;

import java.math.BigDecimal;

public record AccountDto(Integer id, String account, String clientId, BigDecimal funds, boolean isBlocked) {
}