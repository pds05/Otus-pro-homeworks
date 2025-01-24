package ru.otus.java.pro.homeworks.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "products")

@NamedQueries({
        @NamedQuery(name = "Promotion.findAllWithData",
                query = "from Promotion p " +
                        "left join fetch p.products")

})
@NamedEntityGraph(name = "Promotion.products",
        attributeNodes = @NamedAttributeNode(
                value = Promotion_.PRODUCTS,
                subgraph = "Promotion.Product.type"),
        subgraphs = @NamedSubgraph(
                name = "Promotion.Product.type",
                attributeNodes = @NamedAttributeNode(Product_.TYPE)))
@Entity
@Table(name = "PROMOTIONS", schema = "PRODUCTS_STORE")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "Promotion.seq")
    @SequenceGenerator(name = "Promotion.seq",
            sequenceName = "promotions_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "DISCOUNT", nullable = false, precision = 3, scale = 1)
    private BigDecimal discount;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "products_promotions", schema = "PRODUCTS_STORE",
            joinColumns = @JoinColumn(name = "promotions_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new LinkedHashSet<>();

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        products.removeIf(p -> p.getId().equals(product.getId()));
    }

}