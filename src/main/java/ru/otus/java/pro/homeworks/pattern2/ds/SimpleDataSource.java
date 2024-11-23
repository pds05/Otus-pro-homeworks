package ru.otus.java.pro.homeworks.pattern2.ds;

import ru.otus.java.pro.homeworks.pattern2.ApplicationException;

import java.sql.*;

public class SimpleDataSource implements DataSource {
    private static SimpleDataSource dataSource;
    private final String url;
    private Connection connection;

    private SimpleDataSource(String url) {
        this.url = url;
        open();
        init();
    }

    public static SimpleDataSource getInstance(String url) {
        if (dataSource == null) {
            dataSource = new SimpleDataSource(url);
        }
        return dataSource;
    }


    @Override
    public void open() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(url);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ApplicationException("Не уадлось открыть соединение");
            }
        }
    }

    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("SimpleDataSource закрыт");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ApplicationException("Не уадлось закрыть соединение");
            }
        }
    }

    public void init() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("" +
                    "create table if not exists items (" +
                    "id bigserial primary key," +
                    "title varchar(255)," +
                    "price decimal);");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationException("Не уадлось выполнить запрос");
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
