package ru.otus.java.pro.spring.app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCOUNTS")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "ACCOUNT", length = 12)
    private String account;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CLIENT_ID", updatable = false, insertable = false)
    private Client client;

    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "FUNDS", precision = 10, scale = 2)
    private BigDecimal funds;

    @Column(name = "IS_BLOCKED")
    private boolean isBlocked;

}