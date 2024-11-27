package ru.otus.java.pro.homeworks.pattern1;

public interface Assemble<T> {

    void put(T item);

    T get();

    T get(int innerSize);

    int amount();
}
