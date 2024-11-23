package ru.otus.java.pro.homeworks.pattern2;

public class ApplicationException extends RuntimeException {
    public ApplicationException() {}

    public ApplicationException(String message) {
        super(message);
    }
}
