package ru.otus.java.pro.homeworks.spring.product_service.repository;

import java.util.List;
import java.util.Optional;

public interface SimpleRepository<T> {
    List<T> getAll();

    Optional<T> get(Long id);

    T add(T entity);

    T update(T entity);

    boolean delete(T id);
}
