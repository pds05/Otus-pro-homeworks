package ru.otus.java.pro.homeworks.spring.product_service.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.java.pro.homeworks.spring.product_service.entity.ProductCategory;

import java.util.Optional;

public interface ProductCategoriesRepository extends CrudRepository<ProductCategory, Long> {

    Optional<ProductCategory> findByTitle(String name);
}
