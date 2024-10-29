package ru.otus.java.pro.homeworks.concurrent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CustomThreadsPool implements ThreadExecutorService {
    private int poolSize;
    private Queue<Runnable> tasksQueue;
    private List<TaskWorker> workers;
    private boolean isWorking = true;

    public CustomThreadsPool(int poolSize) {
        this.poolSize = poolSize;
        init();
    }

    private void init() {
        workers = new ArrayList<>();
        tasksQueue = new LinkedList<>();
        for (int i = 0; i < poolSize; i++) {
            TaskWorker worker = new TaskWorker(tasksQueue, "task-thread-" + i);
            worker.start();
            workers.add(worker);
        }
    }

    @Override
    public void execute(Runnable r) {
        if (isWorking) {
            tasksQueue.offer(r);
        } else {
            throw new IllegalStateException("Pool is shutdown");
        }

    }

    @Override
    public void shutdown() {
        isWorking = false;
        for (TaskWorker worker : workers) {
            worker.shutdown();
        }
        System.out.println("Pool shutdown");
    }
}
