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

        Cart cart = context.getBean(Cart.class);
        cart.addProduct(1L);
        cart.addProduct(2L);
        cart.addProduct(11L);

        System.out.println("Full cart: " + cart.getProducts());

        cart.removeProduct(2L);
        System.out.println("Removed product from cart: " + cart.getProducts());

        cart.removeAll();
        System.out.println("Empty cart: " + cart.getProducts());

        System.out.println("Repository: " + repository.getAll());
    }
}
