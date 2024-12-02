package ru.otus.java.pro.homeworks.spring.repository;

import java.util.List;

public interface SimpleRepository<T> {

    List<T> getAll();

    T get(long id);

    T add(T entity);
}
