package ru.otus.java.pro.mt.core.transfers.exceptions_handling;

import java.util.List;

public class ValidationException extends RuntimeException {
    private String code;
    private List<ValidationFieldError> errors;

    public String getCode() {
        return code;
    }

    public List<ValidationFieldError> getErrors() {
        return errors;
    }

    public ValidationException(String code, String message, List<ValidationFieldError> errors) {
        super(message);
        this.code = code;
        this.errors = errors;
    }
}
