package ru.otus.java.pro.homeworks.pattern2.dao;

import ru.otus.java.pro.homeworks.pattern2.ds.DataSource;

public interface AppDao<T> {

    T create(T item);

    T update(T item);

    boolean delete(T item);

    T find(long id);

    DataSource getDataSource();

    void close();
}
