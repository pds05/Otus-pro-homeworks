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
import ru.otus.java.pro.homeworks.hibernate.daos.ProductDao;
import ru.otus.java.pro.homeworks.hibernate.daos.PromotionDao;
import ru.otus.java.pro.homeworks.hibernate.daos.UserDao;
import ru.otus.java.pro.homeworks.hibernate.dtos.ProductDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.ProductTypeDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.PromotionDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.UserProfileDto;
import ru.otus.java.pro.homeworks.hibernate.entities.Product;
import ru.otus.java.pro.homeworks.hibernate.entities.ProductType;
import ru.otus.java.pro.homeworks.hibernate.entities.Promotion;
import ru.otus.java.pro.homeworks.hibernate.entities.User;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;
import ru.otus.java.pro.homeworks.hibernate.util.EntityDtoMapper;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
public class ManagerService {
    public static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

    private UserDao userDao;
    private ProductDao productDao;
    private PromotionDao promotionDao;

    public ProductDto addProduct(ProductDto productDto, String categoryTitle) {
        Product product;
        if (productDto.getProductId() != null) {
            product = productDao.findById(productDto.getProductId())
                    .orElseThrow(() -> new ApplicationException("Product id " + productDto.getProductId() + " not found"));
            product.setQuantity(product.getQuantity() + productDto.getQuantity());
            productDao.update(product);
        } else {
            product = EntityDtoMapper.map(productDto);
            ProductType productType = productDao.findProductTypeByTitle(categoryTitle).orElseThrow(() -> new ApplicationException("Product type " + categoryTitle + " not found"));
            product.setType(productType);
            productDao.save(product);
            if (productDto.getPromotionId() != null) {
                Promotion promotion = promotionDao.findById(productDto.getPromotionId())
                        .orElseThrow(() -> new ApplicationException("Promotion id " + productDto.getPromotionId() + " not found"));
                promotion.addProduct(product);
                promotionDao.update(promotion);
            }
        }
        //reload product for calculating actual price
        product = productDao.findById(product.getId()).orElseThrow(() -> new ApplicationException("Error product loading by id"));
        logger.info("Added product {}", product);
        return EntityDtoMapper.map(product);
    }

    public void removeProduct(long productId) {
        Product product = productDao.findById(productId).orElseThrow(() -> new ApplicationException("Product id " + productId + " not found"));
        productDao.delete(productId);
        logger.info("Removed product {}", product);
    }

    public void removeProduct(long productId, int quantity) {
        Product product = productDao.findById(productId).orElseThrow(() -> new ApplicationException("Product id " + productId + " not found"));
        if (product.getQuantity() < quantity) {
            removeProduct(productId);
        } else {
            product.setQuantity(product.getQuantity() - quantity);
            productDao.update(product);
        }
        logger.info("Updated product {}", product);
    }

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

    public List<PromotionDto> viewAllPromotions() {
        List<Promotion> promotions = promotionDao.findAll();
        return promotions.stream().map(EntityDtoMapper::map).toList();
    }

    public PromotionDto findPromotionById(long promotionId) {
        Promotion promotion = promotionDao.findById(promotionId).orElseThrow(() -> new ApplicationException("Promotion not found"));
        return EntityDtoMapper.map(promotion);
    }

    public PromotionDto addPromotion(PromotionDto promotionDto) {
        Promotion promotion = EntityDtoMapper.map(promotionDto);
        if (promotionDto.getProducts() != null) {
            Set<Product> products = promotionDto.getProducts().stream()
                    .map(p -> productDao.findById(p.getProductId()).orElse(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            promotion.setProducts(products);
        }
        Promotion savedPromotion = promotionDao.update(promotion);
        logger.info("Added promotion {}", savedPromotion);
        return EntityDtoMapper.map(savedPromotion);
    }

    public void removePromotion(long promotionId) {
        Promotion promotion = promotionDao.findById(promotionId).orElseThrow(() -> new ApplicationException("Promotion not found"));
        promotionDao.delete(promotionId);
        logger.info("Removed promotion {}", promotion);
    }

    public void addProductsToPromotion(List<Long> productIds, long promotionId) {
        Promotion promotion = promotionDao.findById(promotionId).orElseThrow(() -> new ApplicationException("Promotion not found"));
        Transaction transaction = getSession().beginTransaction();
        try {
            productIds.forEach(productId -> {
                Product product = productDao.findById(productId).orElseThrow(() -> new ApplicationException("Product " + productId + " not found"));
                promotion.getProducts().add(product);
            });
            promotionDao.update(promotion);
            transaction.commit();
            logger.info("Added products {} to promotion, updated promotion {}", productIds, promotion);
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to add products to promotionId={}, productIds={}", productIds, productIds, e);
            throw new ApplicationException("Failed to add products to promotion, cause=" + e.getMessage());
        }
    }

    public void removeProductsFromPromotion(List<Long> productIds, long promotionId) {
        Promotion promotion = promotionDao.findById(promotionId).orElseThrow(() -> new ApplicationException("Promotion not found"));
        Transaction transaction = getSession().beginTransaction();
        try {
            productIds.forEach(productId -> {
                Product product = productDao.findById(productId).orElseThrow(() -> new ApplicationException("Product " + productId + " not found"));
                promotion.removeProduct(product);
            });
            promotionDao.update(promotion);
            transaction.commit();
            logger.info("Removed products {} from promotion, updated promotion {}", productIds, promotion);
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to remove products from promotion, promotionId={}, productIds={}", promotionId, productIds, e);
            throw new ApplicationException("Failed to remove products from promotion, cause=" + e.getMessage());
        }
    }

    public List<UserProfileDto> findUsersByProduct(long productId) {
        List<User> users = userDao.findByOrdersProductId(productId);
        return users.stream().map(EntityDtoMapper::map).toList();
    }

    private Session getSession() {
        SessionFactory sessionFactory = SessionFactoryConfigurator.configInstance();
        return sessionFactory.getCurrentSession();
    }
}
