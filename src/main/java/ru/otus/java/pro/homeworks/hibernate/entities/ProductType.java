package ru.otus.java.pro.homeworks.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "products")

@NamedQueries({
        @NamedQuery(name = "ProductType.findByTitleWithData",
                query = "from ProductType pt " +
                        "where pt.title = :title")
})

@Entity
@Table(name = "PRODUCT_TYPE", schema = "PRODUCTS_STORE")
public class ProductType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "ProductType.seq")
    @SequenceGenerator(name = "ProductType.seq",
            sequenceName = "product_type_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 20)
    private String title;

    @ToString.Exclude
    @OneToMany(mappedBy = Product_.TYPE, fetch = FetchType.LAZY)
    private Set<Product> products = new LinkedHashSet<>();

}