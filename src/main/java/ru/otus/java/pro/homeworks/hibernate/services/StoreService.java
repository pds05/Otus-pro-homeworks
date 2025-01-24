package ru.otus.java.pro.homeworks.hibernate.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.homeworks.hibernate.config.SessionFactoryConfigurator;
import ru.otus.java.pro.homeworks.hibernate.daos.OrderDao;
import ru.otus.java.pro.homeworks.hibernate.daos.ProductDao;
import ru.otus.java.pro.homeworks.hibernate.dtos.Basket;
import ru.otus.java.pro.homeworks.hibernate.dtos.ProductDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.ProductTypeDto;
import ru.otus.java.pro.homeworks.hibernate.entities.*;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;
import ru.otus.java.pro.homeworks.hibernate.util.EntityDtoMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class StoreService {
    public static final Logger logger = LoggerFactory.getLogger(StoreService.class);
    private static Map<Long, Basket> USER_BASKETS = new HashMap<>();

    private OrderDao orderDao;
    private ProductDao productDao;
    private AuthenticationProvider authenticationProvider;

    public List<ProductDto> viewAllProducts() {
        List<Product> products = productDao.findAll();
        System.out.println(products);
        return products.stream().map(EntityDtoMapper::map).toList();
    }

    public List<ProductDto> viewProductsInCategory(String category) {
        List<Product> products = productDao.findByProductType(category);
        return products.stream().map(EntityDtoMapper::map).toList();
    }

    public ProductDto findProductByTitle(String title) {
        Product product = productDao.findByTitle(title)
                .orElseThrow(() -> new ApplicationException("Product not found"));
        return EntityDtoMapper.map(product);
    }

    public List<ProductTypeDto> viewAllProductTypes() {
        List<ProductType> productTypes = productDao.findProductTypes();
        return productTypes.stream().map(EntityDtoMapper::map).toList();
    }

    public void addProductToBasket(long userId, long productId, int quantity) {
        if (!authenticationProvider.isAuthenticated(userId)) throw new ApplicationException("First need to login");
        productDao.findById(productId).orElseThrow(() -> new ApplicationException("Product not found"));
        Basket basket = USER_BASKETS.getOrDefault(userId, new Basket(userId));
        basket.addProduct(productId, quantity);
        USER_BASKETS.putIfAbsent(userId, basket);
    }

    public void addProductToBasket(long userId, String productTitle, int quantity) {
        if (!authenticationProvider.isAuthenticated(userId)) throw new ApplicationException("First need to login");
        Product product = productDao.findByTitle(productTitle.toLowerCase()).orElseThrow(() -> new ApplicationException("Product not found"));
        Basket basket = USER_BASKETS.getOrDefault(userId, new Basket(userId));
        basket.addProduct(product.getId(), quantity);
        USER_BASKETS.putIfAbsent(userId, basket);

    }

    public void removeProductFromBasket(long userId, long productId) {
        if (!authenticationProvider.isAuthenticated(userId)) throw new ApplicationException("First need to login");
        Basket basket = USER_BASKETS.getOrDefault(userId, new Basket(userId));
        basket.removeProduct(productId);
    }

    public Basket viewBasket(long userId) {
        if (!authenticationProvider.isAuthenticated(userId)) throw new ApplicationException("First need to login");
        return USER_BASKETS.getOrDefault(userId, new Basket(userId));
    }

    public void clearBasket(long userId) {
        USER_BASKETS.remove(userId);
    }

    public void makeOrder(long userId) {
        if (!authenticationProvider.isAuthenticated(userId)) throw new ApplicationException("First need to login");
        if (!USER_BASKETS.containsKey(userId)) {
            throw new ApplicationException("Basket is empty");
        }
        Basket basket = USER_BASKETS.get(userId);
        Transaction transaction = getSession().beginTransaction();
        try {
            User user = new User();
            user.setId(userId);

            Order order = new Order();
            order.setUser(user);
            order.setOrderDatetime(LocalDateTime.now());
            order.setTotalAmount(BigDecimal.ZERO);

            basket.getProducts().forEach((productId, quantity) -> {
                Product product = productDao.findById(productId).orElseThrow(() -> new ApplicationException("Product {} from basket not found"));
                if (product.getQuantity() == 0 || product.getQuantity() < quantity) {
                    logger.warn("Not enough product {}, current quantity {}, required quantity {}",
                            product.getTitle(),
                            product.getQuantity(),
                            quantity);
                    return;
                }
                OrdersProduct ordersProduct = new OrdersProduct();
                ordersProduct.setOrder(order);
                ordersProduct.setProduct(product);
                ordersProduct.setQuantity(quantity);
                ordersProduct.setOrderPrice(product.getActualPrice());
                ordersProduct.setPriceType(product.getPriceType());
                ordersProduct.setPromotionId(product.getPromotionId());

                order.addOrdersProduct(ordersProduct);
                order.setTotalAmount(order.getTotalAmount()
                        .add(product.getActualPrice().multiply(BigDecimal.valueOf(quantity))));

                product.setQuantity(product.getQuantity() - quantity);
                productDao.update(product);
            });
            Order createdOrder = orderDao.update(order);
            transaction.commit();

            logger.info("New order received {}", createdOrder);
            USER_BASKETS.remove(userId);
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to create order, userId={}", userId, e);
            throw new ApplicationException("Failed to create order, cause={}" + e.getMessage());
        }
    }

    private Session getSession() {
        SessionFactory sessionFactory = SessionFactoryConfigurator.configInstance();
        return sessionFactory.getCurrentSession();
    }
}
