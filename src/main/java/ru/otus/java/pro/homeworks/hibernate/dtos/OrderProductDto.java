package ru.otus.java.pro.homeworks.hibernate.dtos;

import lombok.*;
import ru.otus.java.pro.homeworks.hibernate.lib.PriceType;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class OrderProductDto {
    private long orderId;
    private long productId;
    private String productTitle;
    private int quantity;
    private BigDecimal price;
    private PriceType priceType;
}
