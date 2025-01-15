package ru.otus.java.pro.homeworks.spring.product_service.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "PRODUCT_DETAILS")
public class ProductDetails {
    public static final String DELIVERY_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Id
    @Column("PRODUCT_ID")
    private Long productId;
    @Column("DESCRIPTION")
    private String description;
    @Column("PROVIDER")
    private String provider;
    @Column("DELIVERY_DATE")
    private LocalDateTime deliveryDate;

    public void setDeliveryDate(String deliveryDate) {
        if (deliveryDate != null && !deliveryDate.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DELIVERY_DATETIME_FORMAT);
            this.deliveryDate = LocalDateTime.parse(deliveryDate, formatter);
        } else {
            this.deliveryDate = LocalDateTime.now();
        }

    }

    public String getDeliveryDate() {
        return deliveryDate != null ? deliveryDate.format(DateTimeFormatter.ofPattern(DELIVERY_DATETIME_FORMAT)) : null;
    }

    public void update(ProductDetails productDetails) {
        if (productDetails != null) {
            if (productDetails.getProductId() != null) this.productId = productDetails.getProductId();
            if (productDetails.getProvider() != null) this.provider = productDetails.getProvider();
            if (productDetails.getDescription() != null) this.description = productDetails.getDescription();
            if (productDetails.getDeliveryDate() != null) setDeliveryDate(productDetails.getDeliveryDate());
        }
    }
}
