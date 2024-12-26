package ru.otus.java.pro.homeworks.hibernate;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.homeworks.hibernate.config.SessionFactoryConfigurator;
import ru.otus.java.pro.homeworks.hibernate.daos.*;
import ru.otus.java.pro.homeworks.hibernate.dtos.Basket;
import ru.otus.java.pro.homeworks.hibernate.dtos.OrderDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.ProductDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.UserProfileDto;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;
import ru.otus.java.pro.homeworks.hibernate.services.StoreService;
import ru.otus.java.pro.homeworks.hibernate.services.UserService;

import java.util.List;

public class MyStoreApp {
    public static final Logger logger = LoggerFactory.getLogger(MyStoreApp.class);

    public static void main(String[] args) {
        try (SessionFactory sessionFactory = SessionFactoryConfigurator.configInstance()) {
            UserDao userDao = new UserDaoImpl(sessionFactory);
            OrderDao orderDao = new OrderDaoImpl(sessionFactory);
            ProductDao productDao = new ProductDaoImpl(sessionFactory);
            UserService userService = new UserService(userDao, orderDao);
            StoreService storeService = new StoreService(orderDao, productDao, userService);

            List<ProductDto> breads = storeService.viewProductsInCategory("Хлеб и выпечка");
            logger.info(breads.toString());
            List<ProductDto> fruits = storeService.viewProductsInCategory("Овощи и фрукты");
            logger.info(fruits.toString());

            UserProfileDto user = userService.createProfile("newuser", "pass", "79998887766", "newuser@mail.ru");
            logger.info("User {}", user);
            user = userService.login("newuser", "pass");

            storeService.addProductToBasket(user.getUserId(), breads.get(0).getProductId(), 1);
            storeService.addProductToBasket(user.getUserId(), breads.get(0).getProductId(), 2);
            storeService.addProductToBasket(user.getUserId(), breads.get(1).getProductId(), 2);
            storeService.addProductToBasket(user.getUserId(), fruits.get(0).getProductId(), 5);
            Basket basket = storeService.viewBasket(user.getUserId());
            logger.info("Users basket size={}, products={}", basket.getProducts().size(), basket.getProducts());
            storeService.makeOrder(user.getUserId());

            storeService.addProductToBasket(user.getUserId(), fruits.get(1).getProductId(), 5);
            basket = storeService.viewBasket(user.getUserId());
            logger.info("Users basket size={}, products={}", basket.getProducts().size(), basket.getProducts());
            storeService.makeOrder(user.getUserId());

            List<OrderDto> orders = userService.getOrders(user.getUserId());
            logger.info("Users orders size={}, products={}", orders.size(), orders);

            userService.removeOrder(user.getUserId(), orders.get(0).getOrderId());
            orders = userService.getOrders(user.getUserId());
            logger.info("Users orders after 1st removed size={}, products={}", orders.size(), orders);
        } catch (ApplicationException e) {
            logger.error(e.getMessage());
        }
    }
}
