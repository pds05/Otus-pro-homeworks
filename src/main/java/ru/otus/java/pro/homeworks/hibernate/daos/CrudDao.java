package ru.otus.java.pro.homeworks.hibernate.daos;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {

    Optional<T> findById(long id);

    List<T> findAll();

    T save(T entity);

    T update(T entity);

    void delete(long id);

    default DaoTransaction getTransaction(Session session) {
        Transaction transaction = session.getTransaction();
        if (!TransactionStatus.ACTIVE.equals(transaction.getStatus())) {
            transaction = session.beginTransaction();
            return new DaoTransaction(transaction, true);
        }
        return new DaoTransaction(transaction, false);
    }

}
