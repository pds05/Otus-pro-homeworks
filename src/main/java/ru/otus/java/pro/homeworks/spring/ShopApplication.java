package ru.otus.java.pro.homeworks.spring;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.java.pro.homeworks.spring.bean.Cart;
import ru.otus.java.pro.homeworks.spring.entity.Product;
import ru.otus.java.pro.homeworks.spring.repository.ProductRepository;

public class ShopApplication {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ShopConfiguration.class);
        ProductRepository repository = context.getBean(ProductRepository.class);
        System.out.println("Initial repository: " + repository.getAll());

        Product kvass = new Product(null, "Kvass", 50);
        repository.add(kvass);

        System.out.println("Updated repository: " + repository.getAll());

        Cart cart1 = context.getBean(Cart.class);
        cart1.addProduct(1L);
        cart1.addProduct(2L);
        cart1.addProduct(11L);

        Cart cart2 = context.getBean(Cart.class);
        cart2.addProduct(5L);

        System.out.println("Identity equal cart1 and cart2 - " + (cart1 == cart2));
        System.out.println("Value equal cart1 and cart2 - " + cart1.equals(cart2));
        System.out.println("Cart1: " + cart1.getProducts());
        System.out.println("Cart2: " + cart2.getProducts());

        cart1.removeProduct(2L);
        System.out.println("Removed product from cart1: " + cart1.getProducts());

        cart1.removeAll();
        System.out.println("Empty cart1: " + cart1.getProducts());

        System.out.println("Repository: " + repository.getAll());
    }
}
