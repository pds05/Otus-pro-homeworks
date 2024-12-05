package ru.otus.java.pro.homeworks.spring.product_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String title;
    private int price;
}
