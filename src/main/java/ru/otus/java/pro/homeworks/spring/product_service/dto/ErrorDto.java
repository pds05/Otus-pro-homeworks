package ru.otus.java.pro.homeworks.spring.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDto {
    private int statusCode;
    private String message;
    private LocalDateTime dateTime;

    public ErrorDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.dateTime = LocalDateTime.now();
    }
}
