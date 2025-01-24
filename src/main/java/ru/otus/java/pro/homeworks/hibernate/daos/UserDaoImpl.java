package ru.otus.java.pro.homeworks.hibernate.daos;

import jakarta.persistence.NoResultException;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.homeworks.hibernate.entities.User;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;

import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    public static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private final SessionFactory sessionFactory;

    @Override
    public Optional<User> findById(long id) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        User user = session.find(User.class, id);
        transaction.commit();
        return Optional.ofNullable(user);
    }


    @Override
    public Optional<User> findByCredentials(String username, String password) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        Query<User> namedQuery = session.createNamedQuery("User.findByUsernameAndPassword", User.class)
                .setParameter("username", username)
                .setParameter("password", password);
        Optional<User> user = namedQuery.uniqueResultOptional();
        transaction.commit();
        return user;
    }

    @Override
    public Optional<User> findByIdWithData(long id) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        RootGraph<User> graph = session.createEntityGraph(User.class, "User.orders");
        Optional<User> user = session.byId(User.class)
                .withFetchGraph(graph)
                .loadOptional(id);
        transaction.commit();
        return user;
    }


    @Override
    public Optional<User> findByContacts(String phoneNumber, String email) {
        Optional<User> result;
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        Query<User> namedQuery = session.createNamedQuery("User.findByPhoneNumberOrEmail", User.class)
                .setParameter(1, phoneNumber)
                .setParameter(2, email);
        try {
            result = Optional.of(namedQuery.getSingleResult());
        } catch (NoResultException e) {
            result = Optional.empty();
        }
        transaction.commit();
        return result;
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        List<User> users = session.createQuery("from User", User.class).list();
        transaction.commit();
        return users;
    }

    @Override
    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        try {
            session.persist(user);
            transaction.commit();
            logger.debug("Saved user {} ", user);
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to save user {}", user, e);
            throw new ApplicationException("Failed to save user");
        }
        return user;
    }

    @Override
    public User update(User user) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        try {
            User mergedUser = session.merge(user);
            transaction.commit();
            logger.debug("Updated user {}", mergedUser);
            return mergedUser;
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to update user {}", user, e);
            throw new ApplicationException("Failed to update user");
        }
    }

    @Override
    public void delete(long userId) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        try {
            MutationQuery query = session.createMutationQuery("delete from User where id = :id");
            query.setParameter("id", userId);
            query.executeUpdate();
            transaction.commit();
            logger.debug("Deleted user id={}", userId);
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to delete userId={}", userId, e);
            throw new ApplicationException("Failed to delete user");
        }
    }

    public List<User> findByOrdersProductId(long productId) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        List<User> users = session.createNamedQuery("User.findByProductId", User.class)
                .setParameter("productId", productId)
                .list();
        transaction.commit();
        return users;
    }
}
