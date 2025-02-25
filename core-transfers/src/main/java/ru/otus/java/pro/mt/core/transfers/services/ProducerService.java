package ru.otus.java.pro.mt.core.transfers.services;

public interface ProducerService {

    void send(String topic, Object message);
}
