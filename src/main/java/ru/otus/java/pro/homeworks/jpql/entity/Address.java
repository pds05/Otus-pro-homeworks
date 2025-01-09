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
@Table(name = "CLIENT_ADDRESSES")
public class Address {
    @Id
    @OneToOne
    @JoinColumn(name = "CLIENT_ID")
    @ToString.Exclude
    private Client client;

    @Column(name = "STREET", nullable = false)
    private String street;
}
