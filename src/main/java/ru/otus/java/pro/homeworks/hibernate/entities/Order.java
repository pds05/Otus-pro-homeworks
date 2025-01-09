package ru.otus.java.pro.homeworks.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "ordersProducts")

@NamedQueries({
        @NamedQuery(name = "Order.findAllWithData",
                query = "from Order o " +
                        "join fetch o.user " +
                        "left join fetch o.ordersProducts op " +
                        "left join fetch op.product"),
        @NamedQuery(name = "Order.findByUserId",
                query = "from Order o " +
                        "join fetch o.user u " +
                        "where u.id = :userId")
})
@NamedEntityGraph(
        name = "Order.ordersProducts",
        attributeNodes = @NamedAttributeNode(
                value = Order_.ORDERS_PRODUCTS,
                subgraph = "Order.UserProduct.product"),
        subgraphs = @NamedSubgraph(
                name = "Order.UserProduct.product",
                attributeNodes = @NamedAttributeNode(OrdersProduct_.PRODUCT))
)
@Entity
@Table(name = "ORDERS", schema = "PRODUCTS_STORE")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Order.seq")
    @SequenceGenerator(name = "Order.seq",
            sequenceName = "orders_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "ORDER_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderDatetime;

    @Column(name = "TOTAL_AMOUNT", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = OrdersProduct_.ORDER, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrdersProduct> ordersProducts = new LinkedHashSet<>();

    public void addOrdersProduct(OrdersProduct ordersProduct) {
        ordersProducts.add(ordersProduct);
    }

    public boolean removeOrdersProduct(OrdersProduct ordersProduct) {
        return ordersProducts.removeIf(p -> p.getId().equals(ordersProduct.getId()));
    }
}