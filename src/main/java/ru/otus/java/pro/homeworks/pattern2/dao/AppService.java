package ru.otus.java.pro.homeworks.pattern2.dao;

import java.util.List;

public interface AppService<T> {

    List<T> createItems(int total);

    void increasePrice(List<Long> itemIds, int order);

    AppDao<T> getDao();
}
