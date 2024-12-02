package ru.otus.java.pro.homeworks.spring;

import org.springframework.context.annotation.*;
import ru.otus.java.pro.homeworks.spring.bean.Cart;
import ru.otus.java.pro.homeworks.spring.entity.Product;
import ru.otus.java.pro.homeworks.spring.repository.SimpleRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ComponentScan
@Configuration
public class ShopConfiguration {

    @Scope(value = "prototype")
    @Bean
    public Cart cart(SimpleRepository<Product> repository) {
        return new Cart(repository);
    }

    @Bean
    public List<Product> repositoryProducts() {
        return new ArrayList<>(Arrays.asList(
                new Product(1L, "Milk", 50),
                new Product(2L, "Bread", 10),
                new Product(3L, "Tomatoes", 200),
                new Product(4L, "Cheese", 500),
                new Product(5L, "Sausage", 1000),
                new Product(6L, "Sweet", 300),
                new Product(7L, "Soda", 150),
                new Product(8L, "Potatoes", 20),
                new Product(9L, "Sugar", 100),
                new Product(10L, "Salt", 5)
        ));
    }

    @Scope("prototype")
    @Bean
    public List<Product> cartProducts() {
        return new ArrayList<>();
    }

    @DependsOn("repositoryProducts")
    @Bean("counter")
    public int productsCounter() {
        return repositoryProducts().size();
    }
}
