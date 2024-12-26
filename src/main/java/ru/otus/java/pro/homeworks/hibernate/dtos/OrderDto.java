package ru.otus.java.pro.homeworks.hibernate.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
public class OrderDto {
    private long orderId;
    private long userProfileId;
    private LocalDateTime orderDateTime;
    private BigDecimal totalAmount;
    private List<OrderProductDto> products = new ArrayList<>();

    public OrderProductDto getProductByTitle(long productId) {
        return products.stream().filter(p -> p.getProductId() == productId).findFirst().orElse(null);
    }

    public OrderProductDto getProductByTitle(String title) {
        return products.stream().filter(p -> p.getProductTitle().equals(title)).findFirst().orElse(null);
    }
}
