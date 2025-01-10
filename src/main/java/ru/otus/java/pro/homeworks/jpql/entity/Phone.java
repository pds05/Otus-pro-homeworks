package ru.otus.java.pro.homeworks.jpql.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "CLIENT_PHONES")
public class Phone {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    private Client client;

    @Column(name = "NUMBER", nullable = false)
    private String number;
}
