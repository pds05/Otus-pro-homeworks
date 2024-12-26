package ru.otus.java.pro.homeworks.hibernate.daos;

import ru.otus.java.pro.homeworks.hibernate.entities.Order;

import java.util.List;

public interface OrderDao extends CrudDao<Order> {

    List<Order> findAllByUserId(long userId);
}
