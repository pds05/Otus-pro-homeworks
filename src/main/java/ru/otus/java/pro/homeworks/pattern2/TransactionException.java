package ru.otus.java.pro.homeworks.pattern2;

public class TransactionException extends ApplicationException{
    public TransactionException() {}

    public TransactionException(String message) {
        super(message);
    }
}
