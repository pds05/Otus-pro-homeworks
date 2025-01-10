package ru.otus.java.pro.homeworks.jpql.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString

@NamedEntityGraph(name = "Client.phones",
        attributeNodes = {
                @NamedAttributeNode(value = "phones")
        })

@Entity
@Table(name = "CLIENTS")
public class Client {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, optional = false)
    private Address address;

    public void setAddress(Address address) {
        address.setClient(this);
        this.address = address;
    }

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Phone> phones = new LinkedHashSet<>();

    public void setPhones(Set<Phone> phones) {
        phones.forEach(p -> p.setClient(this));
        this.phones = phones;
    }
}
