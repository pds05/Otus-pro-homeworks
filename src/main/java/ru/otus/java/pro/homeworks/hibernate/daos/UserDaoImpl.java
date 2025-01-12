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
        session.beginTransaction();
        User user = session.find(User.class, id);
        session.getTransaction().commit();
        return Optional.ofNullable(user);
    }


    @Override
    public Optional<User> findByCredentials(String username, String password) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query<User> namedQuery = session.createNamedQuery("User.findByUsernameAndPassword", User.class)
                .setParameter("username", username)
                .setParameter("password", password);
        Optional<User> user = namedQuery.uniqueResultOptional();
        session.getTransaction().commit();
        return user;
    }

    @Override
    public Optional<User> findByIdWithData(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        RootGraph<User> graph = session.createEntityGraph(User.class, "User.orders");
        Optional<User> user = session.byId(User.class)
                .withFetchGraph(graph)
                .loadOptional(id);
        session.getTransaction().commit();
        return user;
    }


    @Override
    public Optional<User> findByContacts(String phoneNumber, String email) {
        Optional<User> result;
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Query<User> namedQuery = session.createNamedQuery("User.findByPhoneNumberOrEmail", User.class)
                .setParameter(1, phoneNumber)
                .setParameter(2, email);
        try {
            result = Optional.of(namedQuery.getSingleResult());
        } catch (NoResultException e) {
            result = Optional.empty();
        }
        session.getTransaction().commit();
        return result;
    }

    @Override
    public List<User> findAll() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<User> users = session.createQuery("from User", User.class).list();
        session.getTransaction().commit();
        return users;
    }

    @Override
    public User save(User user) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            logger.debug("Saved user {} ", user);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to save user {}", user, e);
            throw new ApplicationException("Failed to save user");
        }
        return user;
    }

    @Override
    public User update(User user) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            User mergedUser = session.merge(user);
            session.getTransaction().commit();
            logger.debug("Updated user {}", mergedUser);
            return mergedUser;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to update user {}", user, e);
            throw new ApplicationException("Failed to update user");
        }
    }

    @Override
    public void delete(long userId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            MutationQuery query = session.createMutationQuery("delete from User where id = :id");
            query.setParameter("id", userId);
            query.executeUpdate();
            session.getTransaction().commit();
            logger.debug("Deleted user id={}", userId);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to delete userId={}", userId, e);
            throw new ApplicationException("Failed to delete user");
        }
    }

    public List<User> findByOrdersProductId(long productId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<User> users = session.createNamedQuery("User.findByProductId", User.class)
                .setParameter("productId", productId)
                .list();
        session.getTransaction().commit();
        return users;
    }
}
