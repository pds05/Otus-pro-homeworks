package ru.otus.java.pro.homeworks.spring.product_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.homeworks.spring.product_service.entity.Product;
import ru.otus.java.pro.homeworks.spring.product_service.exception.ResourceNotFoundException;
import ru.otus.java.pro.homeworks.spring.product_service.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.getAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.get(id);
    }

    public Product save(Product product) {
        return productRepository.add(product);
    }

    public Product update(Product product) {
        productRepository.get(product.getId())
                .ifPresentOrElse(
                        entity -> productRepository.update(product)
                        , () -> productRepository.add(product)
                );
        return product;
    }

    public void deleteById(Long id) {
        Product product = productRepository.get(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id=" + id));
        productRepository.delete(product);
    }
}
