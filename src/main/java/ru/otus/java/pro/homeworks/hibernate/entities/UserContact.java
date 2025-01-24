package ru.otus.java.pro.homeworks.hibernate.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")

@Entity
@Table(name = "USER_CONTACTS", schema = "PRODUCTS_STORE")
public class UserContact {
    @Id
    @OneToOne
    private User user;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

}