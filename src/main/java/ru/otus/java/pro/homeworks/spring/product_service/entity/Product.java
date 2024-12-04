package ru.otus.java.pro.homeworks.spring.product_service.entity;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String title;
    private int price;
    private String provider;
    private Date deliveryDate;
}
