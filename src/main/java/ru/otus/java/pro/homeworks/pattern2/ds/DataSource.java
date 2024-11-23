package ru.otus.java.pro.homeworks.pattern2.ds;

import java.sql.Connection;

public interface DataSource {

    void open();

    void close();

    Connection getConnection();
}
