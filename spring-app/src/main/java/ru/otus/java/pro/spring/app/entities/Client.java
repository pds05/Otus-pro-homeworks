package ru.otus.java.pro.spring.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "CLIENTS")
public class Client {
    @Id
    @Column(name = "ID", nullable = false, length = 10)
    private String id;

    @Column(name = "USERNAME", length = 50)
    private String username;

    @ToString.Exclude
    @OneToMany(mappedBy = "client")
    private Set<Account> accounts = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "client")
    private Set<Transfer> transfers = new LinkedHashSet<>();

}