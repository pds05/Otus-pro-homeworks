package ru.otus.java.pro.homeworks.spring.product_service.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.java.pro.homeworks.spring.product_service.entity.ProductDetails;

public interface ProductDetailsRepository extends CrudRepository<ProductDetails, Long> {
}
