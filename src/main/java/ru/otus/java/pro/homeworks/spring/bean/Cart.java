package ru.otus.java.pro.homeworks.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.java.pro.homeworks.spring.entity.Product;
import ru.otus.java.pro.homeworks.spring.repository.SimpleRepository;

import java.util.List;
import java.util.Objects;


public class Cart {

    private SimpleRepository<Product> repository;

    private List<Product> cartProducts;

    @Autowired
    public Cart(SimpleRepository<Product> repository) {
        this.repository = repository;
    }

    public void addProduct(long productId) {
        cartProducts.add(repository.get(productId));
    }

    public void removeProduct(long productId) {
        Product product = repository.get(productId);
        if (product != null) {
            cartProducts.remove(product);
        }
    }

    public void removeAll() {
        cartProducts.clear();
    }

    public List<Product> getProducts() {
        return cartProducts;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Cart cart = (Cart) object;
        return Objects.equals(cartProducts, cart.cartProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cartProducts);
    }
}
