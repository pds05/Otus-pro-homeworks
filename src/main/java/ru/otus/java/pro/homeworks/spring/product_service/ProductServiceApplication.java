package ru.otus.java.pro.homeworks.spring.product_service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.otus.java.pro.homeworks.spring.product_service.repository.ProductsRepository;


@RequiredArgsConstructor
@SpringBootApplication
public class ProductServiceApplication implements CommandLineRunner {
    public static final Logger logger = LoggerFactory.getLogger(ProductServiceApplication.class.getName());

    private final ProductsRepository productsRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Products: {}", productsRepository.findAllWithDetails());
    }

    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
