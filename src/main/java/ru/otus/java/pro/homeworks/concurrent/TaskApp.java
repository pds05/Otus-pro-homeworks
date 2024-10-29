package ru.otus.java.pro.homeworks.concurrent;

public class TaskApp {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadsPool pool = new CustomThreadsPool(5);

        doSomething(pool, 10);
        Thread.sleep(1000);
        doSomething(pool, 100);

        pool.shutdown();
        Thread.sleep(5000);
        doSomething(pool, 10);

    }

    private static void doSomething(CustomThreadsPool pool, int howMuch) {
        int[] taskNumber = new int[1];
        taskNumber[0] = 0;
        for (int i = 0; i < howMuch; i++) {
            pool.execute(() -> {
                taskNumber[0]++;
                System.out.println(Thread.currentThread().getName() + ": task number " + taskNumber[0]);
                try {
                    Thread.sleep((int) (Math.random() * 100));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
