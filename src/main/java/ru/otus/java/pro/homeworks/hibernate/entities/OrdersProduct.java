package ru.otus.java.pro.homeworks.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.otus.java.pro.homeworks.hibernate.lib.PriceType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "promotion")

@Entity
@Table(name = "ORDERS_PRODUCTS", schema = "PRODUCTS_STORE")
public class OrdersProduct {
    @EmbeddedId
    private OrdersProductId id = new OrdersProductId();

    @ToString.Exclude
    @MapsId("orderId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @MapsId("productId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "ORDER_PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRICE_TYPE", nullable = false)
    private PriceType priceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMOTION_ID", updatable = false, insertable = false)
    private Promotion promotion;

    @Column(name = "PROMOTION_ID")
    private Long promotionId;

}