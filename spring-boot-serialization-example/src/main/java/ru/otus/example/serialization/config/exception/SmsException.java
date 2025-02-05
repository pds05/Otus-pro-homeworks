package ru.otus.example.serialization.config.exception;

import lombok.Getter;

@Getter
public class SmsException extends RuntimeException {
    private final String causeCode;

    public SmsException(String causeCode, String message) {
        super(message);
        this.causeCode = causeCode;
    }

    public SmsException(String causeCode, String message, Throwable throwable) {
        super(message);
        this.causeCode = causeCode;
    }
}
