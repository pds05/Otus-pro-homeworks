package ru.otus.java.pro.homeworks.pattern2;

import ru.otus.java.pro.homeworks.pattern2.dao.*;
import ru.otus.java.pro.homeworks.pattern2.ds.DataSource;
import ru.otus.java.pro.homeworks.pattern2.ds.SimpleDataSource;

import java.util.List;
import java.util.stream.Collectors;


public class MainApp {
    public static void main(String[] args) {
        DataSource dataSource;
        AppDao<Item> dao = null;
        try {
            dataSource = SimpleDataSource.getInstance("jdbc:h2:file:./db;MODE=PostgreSQL");
            dao = new ItemsDao(dataSource);
            ItemsServiceProxy proxy = new ItemsServiceProxy(new ItemsService(dao));
            List<Item> items = proxy.createItems(100);
            List<Long> ids = items.stream().map(Item::getId).collect(Collectors.toList());
            proxy.increasePrice(ids, 2);
        } finally {
            if (dao != null) dao.close();
        }
    }
}
