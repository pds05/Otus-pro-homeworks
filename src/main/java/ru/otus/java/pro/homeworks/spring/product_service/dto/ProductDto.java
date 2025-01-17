package ru.otus.java.pro.homeworks.spring.product_service.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String categoryTitle;
    private String productDetailsProvider;
    private String deliveryDate;
}
