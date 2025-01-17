package ru.otus.java.pro.homeworks.spring.product_service.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.pro.homeworks.spring.product_service.entity.Product;
import ru.otus.java.pro.homeworks.spring.product_service.entity.ProductCategory;
import ru.otus.java.pro.homeworks.spring.product_service.entity.ProductDetails;
import ru.otus.java.pro.homeworks.spring.product_service.exception.BadRequestException;
import ru.otus.java.pro.homeworks.spring.product_service.exception.ResourceNotFoundException;
import ru.otus.java.pro.homeworks.spring.product_service.repository.ProductCategoriesRepository;
import ru.otus.java.pro.homeworks.spring.product_service.repository.ProductDetailsRepository;
import ru.otus.java.pro.homeworks.spring.product_service.repository.ProductsRepository;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductsRepository productsRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final ProductCategoriesRepository categoriesRepository;

    @Transactional
    public List<Product> getProducts() {
        return productsRepository.findAllWithDetails();
    }

    @Transactional
    public Optional<ProductCategory> getProductCategory(String title) {
        return categoriesRepository.findByTitle(title);
    }

    @Transactional
    public Optional<Product> getProduct(String title) {
        return productsRepository.findByTitleWithDetail(title);
    }

    @Transactional
    public Optional<Product> getProduct(Long id) {
        return productsRepository.findByIdWithDetail(id);
    }

    @Transactional
    public Product save(Product product) {
        Product savedProduct = productsRepository.save(product);
        ProductDetails details = product.getDetails();
        details.setProductId(savedProduct.getId());
        ProductDetails savedDetails = productDetailsRepository.save(details);
        savedProduct.setDetails(savedDetails);
        savedProduct.setCategory(categoriesRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category id " + product.getCategoryId() + " not found")));
        return savedProduct;
    }

    @Transactional
    public Product update(Product product) {
        if (product.getId() == null) throw new BadRequestException("'id' parameter not found");
        Product oldProduct = productsRepository.findByIdWithDetail(product.getId()).orElseThrow(() -> new ResourceNotFoundException("Product id " + product.getId() + " not found"));
        oldProduct.update(product);
        Product newProduct = productsRepository.save(oldProduct);
        ProductDetails details = productDetailsRepository.save(oldProduct.getDetails());
        newProduct.setDetails(details);
        return newProduct;
    }

    @Transactional
    public void deleteProduct(Long id) {
        productsRepository.deleteById(id);
    }
}
