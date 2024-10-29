package ru.otus.java.pro.homeworks.concurrent;

import java.util.Optional;
import java.util.Queue;

public class TaskWorker extends Thread {
    private final Queue<Runnable> taskQueue;
    private boolean isWorking = true;

    public TaskWorker(Queue<Runnable> taskQueue, String name) {
        super(name);
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        Optional<Runnable> task;
        while (true) {
            if (!isWorking && taskQueue.isEmpty()) {
                System.out.println("TaskWorker " + getName() + " stopped");
                return;
            }
            synchronized (taskQueue) {
                task = Optional.ofNullable(taskQueue.poll());

            }
            task.ifPresent(Runnable::run);
        }
    }

    public void shutdown() {
        System.out.println("TaskWorker " + getName() + " shutting down");
        isWorking = false;
    }
}
