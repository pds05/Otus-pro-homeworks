package ru.otus.java.pro.homeworks.spring.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.java.pro.homeworks.spring.entity.Product;

import java.util.List;

@Repository
public class ProductRepository implements SimpleRepository<Product> {
    @Autowired
    private int counter;

    @Autowired
    private List<Product> repositoryProducts;

    @Override
    public List<Product> getAll() {
        return repositoryProducts;
    }

    @Override
    public Product get(long id) {
        return repositoryProducts.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Product add(Product entity) {
        entity.setId((long) ++counter);
        repositoryProducts.add(entity);
        return entity;
    }
}
