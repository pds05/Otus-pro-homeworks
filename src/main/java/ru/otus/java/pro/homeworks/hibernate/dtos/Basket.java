package ru.otus.java.pro.homeworks.hibernate.dtos;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Basket {
    private long userId;
    private final Map<Long, Integer> products = new HashMap<>();

    public void addProduct(long productId, int quantity) {
        products.merge(productId, quantity, Integer::sum);
    }

    public void removeProduct(long productId) {
        products.remove(productId);
    }
}
