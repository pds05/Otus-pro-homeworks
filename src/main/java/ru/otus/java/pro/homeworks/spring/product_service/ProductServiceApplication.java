package ru.otus.java.pro.homeworks.spring.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import ru.otus.java.pro.homeworks.spring.product_service.entity.Product;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class ProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    public List<Product> repositoryProducts() {
        Date date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        return new ArrayList<>(Arrays.asList(
                new Product(1L, "Milk", 50, "Good Garden", date),
                new Product(2L, "Bread", 10, "Good Garden", date),
                new Product(3L, "Tomatoes", 200, "Good Garden", date),
                new Product(4L, "Cheese", 500, "Good Garden", date),
                new Product(5L, "Sausage", 1000, "Good Garden", date),
                new Product(6L, "Sweet", 300, "Good Garden", date),
                new Product(7L, "Soda", 150, "Good Garden", date),
                new Product(8L, "Potatoes", 20, "Good Garden", date),
                new Product(9L, "Sugar", 100, "Good Garden", date),
                new Product(10L, "Salt", 5, "Good Garden", date)
        ));
    }

    @DependsOn("repositoryProducts")
    @Bean("counter")
    public int productsCounter() {
        return repositoryProducts().size();
    }
}
