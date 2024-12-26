package ru.otus.java.pro.homeworks.hibernate.services;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.otus.java.pro.homeworks.hibernate.config.SessionFactoryConfigurator;
import ru.otus.java.pro.homeworks.hibernate.daos.*;
import ru.otus.java.pro.homeworks.hibernate.dtos.*;
import ru.otus.java.pro.homeworks.hibernate.entities.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StoreServiceTest {
    static SessionFactory sessionFactory;
    static StoreService storeService;
    static ProductDao productDao;
    static OrderDao orderDao;
    static UserService userService;

    static UserProfileDto user;

    @BeforeAll
    public static void init() {
        sessionFactory = SessionFactoryConfigurator.configInstance();
        productDao = new ProductDaoImpl(sessionFactory);
        orderDao = new OrderDaoImpl(sessionFactory);
        UserDao userDao = new UserDaoImpl(sessionFactory);
        userService = new UserService(userDao, orderDao);
        storeService = new StoreService(orderDao, productDao, userService);
    }

    @Test
    public void findAllProducts() {
        List<ProductDto> products = storeService.viewAllProducts();
        System.out.println(products);
        assertNotNull(products);
        assertTrue(!products.isEmpty());
    }

    @Test
    public void findProductByTitleAndId() {
        ProductDto productDto = storeService.findProductByTitle("Молоко");
        System.out.println(productDto);
        assertEquals("Молоко", productDto.getTitle());

        Product product = productDao.findById(productDto.getProductId()).get();
        System.out.println(product);
        assertEquals(productDto.getProductId(), product.getId());
    }

    @Test
    public void findProductsInCategory() {
        List<ProductTypeDto> productTypes = storeService.viewAllProductTypes();
        System.out.println(productTypes);
        assertNotNull(productTypes);
        assertFalse(productTypes.isEmpty());

        List<ProductDto> products = storeService.viewProductsInCategory(productTypes.get(0).getTitle());
        System.out.println(products);
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    @Order(1)
    public void login() {
        user = userService.login("Вася", "123");
        assertNotNull(user);
    }

    @Test
    @Order(2)
    public void addProductToBasket() {
        ProductDto bred = storeService.findProductByTitle("Хлеб");
        assertNotNull(bred);
        ProductDto milk = storeService.findProductByTitle("Молоко");
        assertNotNull(milk);
        ProductDto tomato = storeService.findProductByTitle("Томаты");
        assertNotNull(tomato);
        ProductDto potato = storeService.findProductByTitle("Картофель");
        assertNotNull(potato);

        storeService.addProductToBasket(user.getUserId(), bred.getProductId(), 1);
        storeService.addProductToBasket(user.getUserId(), bred.getProductId(), 2);
        storeService.addProductToBasket(user.getUserId(), milk.getProductId(), 2);
        storeService.addProductToBasket(user.getUserId(), potato.getProductId(), 5);
        storeService.addProductToBasket(user.getUserId(), tomato.getProductId(), 10);

        Basket basket = storeService.viewBasket(user.getUserId());
        assertFalse(basket.getProducts().isEmpty());
        assertEquals(4, basket.getProducts().size());
    }

    @Test
    @Order(3)
    public void makeOrder() {
        storeService.makeOrder(user.getUserId());

        List<OrderDto> orders = userService.getOrders(user.getUserId());
        OrderDto order = orders.stream()
                .filter(o -> o.getProductByTitle("Хлеб") != null)
                .findFirst().get();

        System.out.println("Order: " + order);
        assertNotNull(order);
        assertEquals(4, order.getProducts().size());
        assertEquals(3, order.getProductByTitle("Хлеб").getQuantity());
        assertEquals(2, order.getProductByTitle("Молоко").getQuantity());
        assertEquals(5, order.getProductByTitle("Картофель").getQuantity());
        assertEquals(10, order.getProductByTitle("Томаты").getQuantity());
    }

    @Order(4)
    @Test
    public void findOrder() {
        List<OrderDto> orders = userService.getOrders(userService.getProfile(1).getUserId());
        OrderDto order = orders.stream()
                .filter(o -> o.getProductByTitle("Хлеб") != null)
                .findFirst().get();
        System.out.println(order);
    }

    @AfterAll
    public static void close() {
        sessionFactory.close();
    }
}
