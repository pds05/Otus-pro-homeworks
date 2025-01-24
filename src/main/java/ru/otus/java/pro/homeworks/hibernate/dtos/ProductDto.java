package ru.otus.java.pro.homeworks.hibernate.dtos;

import lombok.*;
import ru.otus.java.pro.homeworks.hibernate.lib.PriceType;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class ProductDto {
    private Long productId;
    private String title;
    private BigDecimal basePrice;
    private BigDecimal actualPrice;
    private PriceType priceType;
    private int quantity;
    private Long promotionId;
    private String promotionTitle;
    private BigDecimal discount;
}
