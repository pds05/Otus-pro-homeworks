package ru.otus.java.pro.homeworks.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.otus.java.pro.homeworks.hibernate.lib.PriceType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "promotions")

@NamedQueries({
        @NamedQuery(name = "Product.findAllWithData",
                query = "from Product p " +
                        "join fetch p.type " +
                        "left join fetch p.promotions " +
                        "where p.quantity > 0"),
        @NamedQuery(name = "Product.findByProductTypeTitle",
                query = "from Product p " +
                        "join fetch p.type t " +
                        "where t.title = :title " +
                        "and p.quantity > 0"),
        @NamedQuery(name = "Product.findByTitleWithData",
                query = "from Product p " +
                        "join fetch p.type t " +
                        "left join fetch p.promotions " +
                        "where p.quantity > 0 " +
                        "and p.title like :title")
})
@NamedEntityGraph(
        name = "Product.promotions",
        attributeNodes = {
                @NamedAttributeNode(Product_.PROMOTIONS),
                @NamedAttributeNode(Product_.TYPE)
        })
@Entity
@Table(name = "PRODUCTS", schema = "PRODUCTS_STORE")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "Product.seq")
    @SequenceGenerator(name = "Product.seq", sequenceName = "products_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 20)
    private String title;

    @Column(name = "PRICE", nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Transient
    private BigDecimal actualPrice;

    @Transient
    private PriceType priceType;

    @Transient
    private Long promotionId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "TYPE_ID", nullable = false)
    private ProductType type;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "products_promotions", schema = "PRODUCTS_STORE",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "promotions_id"))
    private Set<Promotion> promotions = new LinkedHashSet<>();

    public void addPromotion(Promotion promotion) {
        promotions.add(promotion);
    }

    public boolean removePromotion(Promotion promotion) {
        return promotions.remove(promotion);
    }

    @PostLoad
    public void calculateActualPrice() {
        BigDecimal maxDiscount = promotions
                .stream()
                .map(Promotion::getDiscount)
                .max((o1, o2) -> o1.subtract(o2).intValue())
                .orElse(BigDecimal.ZERO);
        Optional<Promotion> promotion = promotions.stream()
                .filter(p -> p.getDiscount().equals(maxDiscount))
                .findFirst();
        promotion.ifPresentOrElse(p -> {
                    promotionId = p.getId();
                    priceType = PriceType.PROMO;
                }
                , () -> priceType = PriceType.BASE);
        this.actualPrice = new BigDecimal(100)
                .subtract(maxDiscount)
                .divide(new BigDecimal(100), RoundingMode.HALF_UP)
                .multiply(basePrice);
    }
}