package ru.otus.java.pro.homeworks.pattern2.dao;

import ru.otus.java.pro.homeworks.pattern2.ApplicationException;
import ru.otus.java.pro.homeworks.pattern2.TransactionException;
import ru.otus.java.pro.homeworks.pattern2.ds.DataSource;

import java.sql.*;

public class ItemsDao implements AppDao<Item> {
    private DataSource dataSource;
    private PreparedStatement psCreate;
    private PreparedStatement psRead;
    private PreparedStatement psUpdate;
    private PreparedStatement psDelete;

    public ItemsDao(DataSource dataSource) {
        this.dataSource = dataSource;
        try {
            prepareCreate();
            prepareRead();
            prepareUpdate();
            prepareDelete();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationException("Не удалось подготовить запрос preparedStatement");
        }
    }

    private void prepareCreate() throws SQLException {
        psCreate = dataSource.getConnection().prepareStatement("" +
                        "INSERT INTO items (title, price) VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        );
    }

    private void prepareRead() throws SQLException {
        psRead = dataSource.getConnection().prepareStatement("" +
                "SELECT * FROM items WHERE id = ?");
    }

    private void prepareUpdate() throws SQLException {
        psUpdate = dataSource.getConnection().prepareStatement("" +
                "UPDATE items SET title = ?, price = ? " +
                "WHERE id = ?");
    }

    private void prepareDelete() throws SQLException {
        psDelete = dataSource.getConnection().prepareStatement("" +
                "DELETE FROM items WHERE id = ?");
    }

    @Override
    public Item create(Item item) {
        try {
            psCreate.setString(1, item.getTitle());
            psCreate.setBigDecimal(2, item.getPrice());
            psCreate.executeUpdate();
            ResultSet rs = psCreate.getGeneratedKeys();
            if (rs.next()) {
                item.setId(rs.getLong(1));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new TransactionException("Не удалось сохранить сущность " + item);
    }

    @Override
    public Item update(Item item) {
        try {
            psUpdate.setString(1, item.getTitle());
            psUpdate.setObject(2, item.getPrice());
            psUpdate.setLong(3, item.getId());
            psUpdate.executeUpdate();
            return item;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new TransactionException("Не уадлось обновить сущность Item.id=" + item.getId());
    }

    @Override
    public boolean delete(Item item) {
        try {
            psDelete.setLong(1, item.getId());
            return psDelete.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new TransactionException("Не уадлось удалить сущность Item.id=" + item.getId());
    }

    @Override
    public Item find(long id) {
        try {
            psRead.setLong(1, id);
            try (ResultSet rs = psRead.executeQuery()) {
                if (rs.next()) {
                    Item item = new Item(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getBigDecimal("price"));
                    return item;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new TransactionException("Не удалось выплнить запрос выборки Item.id=" + id);
        }
        return null;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void close() {
        try {
            if (psCreate != null) psCreate.close();
            if (psRead != null) psRead.close();
            if (psUpdate != null) psUpdate.close();
            if (psDelete != null) psDelete.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ApplicationException("Не удалось закрыть preparedStatement");
        }
        dataSource.close();
        System.out.println("ItemsDao закрыт");
    }
}
