package ru.otus.java.pro.homeworks.hibernate.daos;

import ru.otus.java.pro.homeworks.hibernate.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends CrudDao<User> {

    Optional<User> findByCredentials(String username, String password);

    Optional<User> findByContacts(String phoneNumber, String email);

    Optional<User> findByIdWithData(long id);

    List<User> findByOrdersProductId(long productId);
}
