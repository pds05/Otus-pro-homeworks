package ru.otus.java.pro.homeworks.spring.product_service.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "PRODUCT_CATEGORIES")
public class ProductCategory {
    @Column("ID")
    @Id
    private Long id;
    @Column("TITLE")
    private String title;
}
