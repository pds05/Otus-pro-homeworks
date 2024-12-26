package ru.otus.java.pro.homeworks.hibernate.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.otus.java.pro.homeworks.hibernate.dtos.*;
import ru.otus.java.pro.homeworks.hibernate.entities.*;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityDtoMapper {

    public static UserProfileDto map(User user) {
        return UserProfileDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .phone(user.getUserContact().getPhoneNumber())
                .email(user.getUserContact().getEmail()).build();
    }

    public static OrderDto map(Order order) {
        return OrderDto.builder()
                .orderId(order.getId())
                .userProfileId(order.getUser().getId())
                .totalAmount(order.getTotalAmount())
                .orderDateTime(order.getOrderDatetime())
                .products(order.getOrdersProducts().stream()
                        .map(op -> OrderProductDto.builder()
                                .orderId(op.getOrder().getId())
                                .productId(op.getProduct().getId())
                                .productTitle(op.getProduct().getTitle())
                                .price(op.getOrderPrice())
                                .priceType(op.getProduct().getPriceType())
                                .quantity(op.getQuantity())
                                .build())
                        .toList())
                .build();
    }

    public static ProductDto map(Product product) {
        Promotion promotion = null;
        if (product.getPromotionId() != null) {
            promotion = product.getPromotions().stream()
                    .filter(p -> p.getId().equals(product.getPromotionId()))
                    .findFirst()
                    .orElseThrow(() -> new ApplicationException("Missing promotion in product"));
        }
        return ProductDto.builder()
                .productId(product.getId())
                .title(product.getTitle())
                .basePrice(product.getBasePrice())
                .actualPrice(product.getActualPrice())
                .priceType(product.getPriceType())
                .quantity(product.getQuantity())
                .promotionId(product.getPromotionId())
                .promotionTitle(promotion == null ? null : promotion.getTitle())
                .discount(promotion == null ? null : promotion.getDiscount())
                .build();
    }

    public static ProductTypeDto map(ProductType productType) {
        return ProductTypeDto.builder()
                .productTypeId(productType.getId())
                .title(productType.getTitle())
                .build();
    }

    public static Product map(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getProductId());
        product.setTitle(productDto.getTitle());
        product.setBasePrice(productDto.getBasePrice());
        product.setActualPrice(productDto.getActualPrice());
        product.setPriceType(productDto.getPriceType());
        product.setQuantity(productDto.getQuantity());
        return product;
    }

    public static Promotion map(PromotionDto promotionDto) {
        Promotion promotion = new Promotion();
        promotion.setId(promotionDto.getPromotionId());
        promotion.setTitle(promotionDto.getTitle());
        promotion.setDiscount(promotionDto.getDiscount());
        if (promotionDto.getProducts() != null) {
            promotion.setProducts(promotionDto.getProducts().stream()
                    .map(EntityDtoMapper::map)
                    .collect(Collectors.toSet()));
        }
        return promotion;
    }

    public static PromotionDto map(Promotion promotion) {
        return PromotionDto.builder()
                .promotionId(promotion.getId())
                .title(promotion.getTitle())
                .discount(promotion.getDiscount())
                .products(promotion.getProducts().stream()
                        .map(EntityDtoMapper::map)
                        .toList())
                .build();
    }
}
