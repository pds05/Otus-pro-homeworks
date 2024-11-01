package ru.otus.java.pro.homeworks.concurrent;

public interface ThreadExecutorService {
    void execute(Runnable r);
    void shutdown();
}
