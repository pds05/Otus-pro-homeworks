package ru.otus.java.pro.homeworks.hibernate.daos;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {

    Optional<T> findById(long id);

    List<T> findAll();

    T save(T entity);

    T update(T entity);

    void delete(long id);

}
