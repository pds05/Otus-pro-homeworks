package ru.otus.java.pro.homeworks.reflection;

import ru.otus.java.pro.homeworks.reflection.api.TestProcessor;

public class TestApp {
    public static void main(String[] args) {
        TestProcessor processor = new TestProcessor(TestCar.class);
        processor.test();
    }
}
