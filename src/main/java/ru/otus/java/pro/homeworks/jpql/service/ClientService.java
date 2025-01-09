package ru.otus.java.pro.homeworks.jpql.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.otus.java.pro.homeworks.jpql.dao.ClientDao;
import ru.otus.java.pro.homeworks.jpql.entity.Address;
import ru.otus.java.pro.homeworks.jpql.entity.Client;
import ru.otus.java.pro.homeworks.jpql.entity.Phone;
import ru.otus.java.pro.homeworks.jpql.exception.ApplicationException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class ClientService {
    private ClientDao dao;

    public Client register(String name, String street, String... phoneNumbers) {
        Address address = new Address();
        address.setStreet(street);

        Set<Phone> phones = Arrays.stream(phoneNumbers).map(phoneNumber -> {
            Phone phone = new Phone();
            phone.setNumber(phoneNumber);
            return phone;
        }).collect(Collectors.toSet());

        Client client = new Client();
        client.setName(name);
        client.setPhones(phones);
        client.setAddress(address);

        return dao.create(client);
    }

    public Client getClient(int id) {
        return dao.findById(id).orElseThrow(() -> new ApplicationException("Client with id=" + id + " not found"));
    }

    public List<Client> getAllClients() {
        return dao.findAll();
    }

    public void updateClient(Client client) {
        client = dao.update(client);
    }

    public void deleteClient(int id) {
        dao.delete(id);
    }
}
