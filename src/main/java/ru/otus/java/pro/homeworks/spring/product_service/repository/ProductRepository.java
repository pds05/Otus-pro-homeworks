package ru.otus.java.pro.homeworks.spring.product_service.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.java.pro.homeworks.spring.product_service.entity.Product;
import ru.otus.java.pro.homeworks.spring.product_service.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

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
    public Optional<Product> get(Long id) {
        return repositoryProducts.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    @Override
    public Product add(Product entity) {
        entity.setId((long) ++counter);
        repositoryProducts.add(entity);
        return entity;
    }

    @Override
    public Product update(Product entity) {
        Product exist = get(entity.getId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return repositoryProducts.set(repositoryProducts.indexOf(exist), entity);
    }

    @Override
    public boolean delete(Product entity) {
        return repositoryProducts.remove(entity);
    }
}
