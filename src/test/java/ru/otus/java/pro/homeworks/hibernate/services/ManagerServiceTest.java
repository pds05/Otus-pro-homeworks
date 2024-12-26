package ru.otus.java.pro.homeworks.hibernate.services;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.otus.java.pro.homeworks.hibernate.config.SessionFactoryConfigurator;
import ru.otus.java.pro.homeworks.hibernate.daos.*;
import ru.otus.java.pro.homeworks.hibernate.dtos.ProductDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.PromotionDto;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ManagerServiceTest {
    static SessionFactory sessionFactory;
    static ProductDao productDao;
    static UserDao userDao;
    static PromotionDao promotionDao;
    static ManagerService managerService;

    @BeforeAll
    public static void init() {
        sessionFactory = SessionFactoryConfigurator.configInstance();
        productDao = new ProductDaoImpl(sessionFactory);
        userDao = new UserDaoImpl(sessionFactory);
        promotionDao = new PromotionDaoImpl(sessionFactory);
        managerService = new ManagerService(userDao, productDao, promotionDao);
    }

    @Test
    public void addProductByBasePrice() {
        ProductDto product = ProductDto.builder()
                .title("Кефир")
                .basePrice(new BigDecimal("45.5"))
                .quantity(10)
                .build();
        ProductDto savedProduct = managerService.addProduct(product, "Молочные продукты");
        assertNotNull(savedProduct.getProductId());

        ProductDto addedProduct = ProductDto.builder()
                .productId(savedProduct.getProductId())
                .quantity(10)
                .build();

        ProductDto updatedProduct = managerService.addProduct(addedProduct, null);
        assertEquals(20, updatedProduct.getQuantity());
    }

    @Test
    public void addProductByPromoPrice() {
        ProductDto product = ProductDto.builder()
                .title("Индейка")
                .basePrice(new BigDecimal(200))
                .quantity(10)
                .promotionId(1L)
                .build();
        ProductDto savedProduct = managerService.addProduct(product, "Мясо и птица");
        assertNotNull(savedProduct.getProductId());
        assertEquals(-1, savedProduct.getActualPrice().compareTo(product.getBasePrice()));
    }

    @Order(1)
    @Test
    public void addPromotion() {
        PromotionDto promotion = PromotionDto.builder()
                .title("За пол цены")
                .discount(new BigDecimal("50"))
                .build();
        PromotionDto savedPromotion = managerService.addPromotion(promotion);
        assertNotNull(savedPromotion.getPromotionId());
    }

    @Order(2)
    @Test
    public void addProductToExistingPromotion() {
        PromotionDto promotion = managerService.viewAllPromotions().stream()
                .filter(p -> p.getTitle().equals("За пол цены"))
                .findFirst().get();
        int promoProducts = promotion.getProducts().size();
        ProductDto product = managerService.findProductByTitle("Конфеты");
        managerService.addProductsToPromotion(Arrays.asList(product.getProductId()), promotion.getPromotionId());
        PromotionDto updatedPromotion = managerService.findPromotionById(promotion.getPromotionId());
        assertNotNull(updatedPromotion.getPromotionId());
        assertEquals(promoProducts + 1, updatedPromotion.getProducts().size());
    }

    @Order(3)
    @Test
    public void removeProductFromPromotion() {
        PromotionDto promotion = managerService.viewAllPromotions().stream()
                .filter(p -> p.getTitle().equals("За пол цены"))
                .findFirst().get();
        int promoProducts = promotion.getProducts().size();
        ProductDto product = managerService.findProductByTitle("Конфеты");
        managerService.removeProductsFromPromotion(Arrays.asList(product.getProductId()), promotion.getPromotionId());
        PromotionDto updatedPromotion = managerService.findPromotionById(promotion.getPromotionId());
        assertNotNull(updatedPromotion.getPromotionId());
        assertEquals(promoProducts - 1, updatedPromotion.getProducts().size());
    }

    @Order(4)
    @Test
    public void removePromotion() {
        PromotionDto promotion = managerService.viewAllPromotions().stream()
                .filter(p -> p.getTitle().equals("За пол цены"))
                .findFirst().get();
        managerService.removePromotion(promotion.getPromotionId());
        assertThatThrownBy(() -> managerService.findPromotionById(promotion.getPromotionId()))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("Promotion not found");
    }

    @Test
    public void addPromotionWithProducts() {
        ProductDto bred = managerService.findProductByTitle("Хлеб");
        ProductDto milk = managerService.findProductByTitle("Молоко");

        PromotionDto promotion = PromotionDto.builder()
                .title("Скидка 20%")
                .discount(new BigDecimal("20"))
                .products(Arrays.asList(bred, milk))
                .build();
        PromotionDto savedPromotion = managerService.addPromotion(promotion);
        assertNotNull(savedPromotion.getPromotionId());
        assertEquals(2, savedPromotion.getProducts().size());
    }

    @Test
    public void removeProduct() {
        ProductDto product = managerService.findProductByTitle("Говядина");
        managerService.removeProduct(product.getProductId());

        ProductDto product2 = managerService.findProductByTitle("Конфеты");
        managerService.removeProduct(product2.getProductId(), 5);
    }

    @AfterAll
    public static void close() {
        sessionFactory.close();
    }
}
