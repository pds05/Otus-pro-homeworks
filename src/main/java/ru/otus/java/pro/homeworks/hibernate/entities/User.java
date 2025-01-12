package ru.otus.java.pro.homeworks.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = "orders")

@NamedQueries({
        @NamedQuery(
                name = "User.findAllWithData",
                query = "from User u " +
                        "join fetch u.userContact " +
                        "left join fetch u.orders"),
        @NamedQuery(
                name = "User.findByUsernameAndPassword",
                query = "from User u " +
                        "join fetch u.userContact uc " +
                        "where u.username = :username and u.password = :password"),
        @NamedQuery(
                name = "User.findByPhoneNumberOrEmail",
                query = "from User u " +
                        "join fetch u.userContact uc " +
                        "where uc.phoneNumber = ?1 or uc.email = ?2"),
        @NamedQuery(
                name = "User.findByProductId",
                query = "select distinct u from User u " +
                        "join Order o on o.user = u " +
                        "join OrdersProduct op on op.order = o " +
                        "where op.product.id = :productId"
        )
})
@NamedEntityGraph(
        name = "User.orders",
        attributeNodes = {
                @NamedAttributeNode(User_.USER_CONTACT),
                @NamedAttributeNode(
                        value = User_.ORDERS,
                        subgraph = "User.Order.ordersProducts"
                )},
        subgraphs = {
                @NamedSubgraph(
                        name = "User.Order.ordersProducts",
                        attributeNodes = @NamedAttributeNode(
                                value = Order_.ORDERS_PRODUCTS,
                                subgraph = "User.Order.OrdersProduct.product")),
                @NamedSubgraph(
                        name = "User.Order.OrdersProduct.product",
                        attributeNodes = @NamedAttributeNode(OrdersProduct_.PRODUCT))
        })
@Entity
@Table(name = "USERS", schema = "PRODUCTS_STORE")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "User.seq")
    @SequenceGenerator(name = "User.seq",
            sequenceName = "users_seq", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 20)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 10)
    private String password;

    @ToString.Exclude
    @OneToMany(mappedBy = Order_.USER, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Order> orders = new LinkedHashSet<>();

    @OneToOne(mappedBy = UserContact_.USER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private UserContact userContact;

    public void setUserContact(UserContact userContact) {
        userContact.setUser(this);
        this.userContact = userContact;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public Optional<Order> getOrder(long orderId) {
        return orders.stream().filter(order -> order.getId().equals(orderId)).findFirst();
    }
}