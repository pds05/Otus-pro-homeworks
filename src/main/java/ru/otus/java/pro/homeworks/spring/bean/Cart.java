package ru.otus.java.pro.homeworks.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.java.pro.homeworks.spring.entity.Product;
import ru.otus.java.pro.homeworks.spring.repository.SimpleRepository;

import java.util.List;


public class Cart {
    @Autowired
    private SimpleRepository<Product> repository;
    @Autowired
    private List<Product> cartProducts;

    public Cart(SimpleRepository<Product> repository) {
        this.repository = repository;
    }

    public void addProduct(long productId) {
        cartProducts.add(repository.get(productId));
    }

    public void removeProduct(long productId) {
        cartProducts.remove(repository.get(productId));
    }

    public void removeAll() {
        cartProducts.clear();
    }

    public List<Product> getProducts() {
        return cartProducts;
    }
}
