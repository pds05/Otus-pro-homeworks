package ru.otus.java.pro.homeworks.jpql.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    T create(T t);

    T update(T t);

    Optional<T> findById(int id);

    List<T> findAll();

    void delete(int id);
}
