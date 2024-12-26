package ru.otus.java.pro.homeworks.hibernate.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class PromotionDto {
    private Long promotionId;
    private String title;
    private BigDecimal discount;
    private List<ProductDto> products;

}
