package ru.otus.java.pro.homeworks.spring.product_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table("PRODUCTS")
public class Product {
    @Id
    @Column("ID")
    private Long id;
    @Column("CATEGORY_ID")
    private Long categoryId;
    @Transient
    @Column("CATEGORY_ID")
    private ProductCategory category;
    @Column("TITLE")
    private String title;
    @Column("PRICE")
    private BigDecimal price;
    @MappedCollection(idColumn = "PRODUCT_ID")
    private ProductDetails details;

    public void update(Product product) {
        if (product != null) {
            if (product.getTitle() != null) this.title = product.getTitle();
            if (product.getPrice() != null) this.price = product.getPrice();
            if (product.getDetails() != null) this.details.update(product.getDetails()); ;
            if (product.getCategory() != null) this.category = product.getCategory();
            if (product.getCategoryId() != null) this.categoryId = product.getCategoryId();
        }
    }
}
