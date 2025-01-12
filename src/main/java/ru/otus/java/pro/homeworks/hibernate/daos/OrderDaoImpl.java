package ru.otus.java.pro.homeworks.hibernate.daos;

import jakarta.persistence.EntityGraph;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.MutationQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.homeworks.hibernate.entities.Order;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderDaoImpl implements OrderDao {
    public static final Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Order> findById(long id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        EntityGraph<?> graph = session.createEntityGraph("Order.ordersProducts");
        Optional<Order> order = session.createQuery("from Order where id = :id", Order.class)
                .setParameter("id", id)
                .setHint("jakarta.persistence.fetchgraph", graph)
                .uniqueResultOptional();
        session.getTransaction().commit();
        return order;
    }

    @Override
    public List<Order> findAll() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Order> orders = session.createNamedQuery("Order.findAllWithData", Order.class)
                .getResultList();
        session.getTransaction().commit();
        return orders;
    }

    @Override
    public List<Order> findAllByUserId(long userId) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        RootGraph<Order> graph = session.createEntityGraph(Order.class, "Order.ordersProducts");
        List<Order> orders = session.createNamedQuery("Order.findByUserId", Order.class)
                .setParameter("userId", userId)
                .applyFetchGraph(graph)
                .list();
        session.getTransaction().commit();
        return orders;
    }

    @Override
    public Order save(Order order) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.persist(order);
            session.getTransaction().commit();
            logger.debug("Saved order {}", order);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to save order {}", order, e);
            throw new ApplicationException("Failed to save order");
        }
        return order;
    }

    @Override
    public Order update(Order order) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            Order mergedOrder = session.merge(order);
            session.getTransaction().commit();
            logger.debug("Updated order {}", mergedOrder);
            return mergedOrder;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to update order {}", order, e);
            throw new ApplicationException("Failed to update order");
        }
    }

    @Override
    public void delete(long orderId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            MutationQuery query = session.createMutationQuery("delete from Order where id = :id");
            query.setParameter("id", orderId);
            query.executeUpdate();
            session.getTransaction().commit();
            logger.debug("Deleted order id={}", orderId);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to delete order id={}", orderId, e);
            throw new ApplicationException("Failed to delete order");
        }
    }
}
