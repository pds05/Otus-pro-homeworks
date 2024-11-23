package ru.otus.java.pro.homeworks.pattern2.dao;

import ru.otus.java.pro.homeworks.pattern2.ApplicationException;
import ru.otus.java.pro.homeworks.pattern2.TransactionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemsServiceProxy implements AppService<Item> {
    private final AppService<Item> service;

    public ItemsServiceProxy(AppService<Item> service) {
        this.service = service;
    }

    @Override
    public List<Item> createItems(int total) {
        List<Item> result = new ArrayList<>();
        Connection connection = service.getDao().getDataSource().getConnection();
        try {
            try {
                connection.setAutoCommit(false);
                result.addAll(service.createItems(total));
                connection.commit();
                return result;
            } catch (TransactionException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationException("Ошибка отмены транзакции");
        }
        return result;
    }

    @Override
    public void increasePrice(List<Long> itemIds, int order) {
        Connection connection = service.getDao().getDataSource().getConnection();
        try {
            try {
                connection.setAutoCommit(false);
                service.increasePrice(itemIds, order);
                connection.commit();
            } catch (TransactionException e) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationException("Ошибка отмены транзакции");
        }
    }

    @Override
    public AppDao<Item> getDao() {
        return service.getDao();
    }
}
