package ru.otus.java.pro.homeworks.hibernate.daos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Transaction;

@Getter
@Setter
@AllArgsConstructor
public class DaoTransaction {

    private Transaction transaction;
    private boolean openedInDao;

    public void commit() {
        if (openedInDao && transaction.isActive()) {
            transaction.commit();
        }
    }

    public void rollback() {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }
}
