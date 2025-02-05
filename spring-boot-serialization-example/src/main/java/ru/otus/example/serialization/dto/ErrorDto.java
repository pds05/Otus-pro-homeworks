package ru.otus.example.serialization.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorDto {
    private String STATUS_CODE;
    private String message;
}
