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
import ru.otus.java.pro.homeworks.hibernate.entities.Promotion;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Getter
@Setter
@RequiredArgsConstructor
public class PromotionDaoImpl implements PromotionDao {
    public static final Logger logger = LoggerFactory.getLogger(PromotionDaoImpl.class);

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Promotion> findById(long id) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        EntityGraph<?> graph = session.createEntityGraph("Promotion.products");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", graph);
        Promotion promotion = session.find(Promotion.class, id, properties);
        transaction.commit();
        return Optional.ofNullable(promotion);
    }

    @Override
    public List<Promotion> findAll() {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        RootGraph<Promotion> graph = session.createEntityGraph(Promotion.class, "Promotion.products");
        List<Promotion> promotions = session.createQuery("from Promotion", Promotion.class)
                .applyFetchGraph(graph)
                .list();
        transaction.commit();
        return promotions;
    }

    @Override
    public Promotion save(Promotion promotion) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        try {
            session.persist(promotion);
            transaction.commit();
            logger.debug("Saved promotion {}", promotion);
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to save promotion {}", promotion, e);
            throw new ApplicationException("Failed to save promotion");
        }
        return promotion;
    }

    @Override
    public Promotion update(Promotion promotion) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        try {
            Promotion mergedPromotion = session.merge(promotion);
            transaction.commit();
            logger.debug("Updated promotion {}", mergedPromotion);
            return mergedPromotion;
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to save promotion {}", promotion, e);
            throw new ApplicationException("Failed to save promotion");
        }
    }

    @Override
    public void delete(long promotionId) {
        Session session = sessionFactory.getCurrentSession();
        DaoTransaction transaction = getTransaction(session);
        try {
            MutationQuery query = session.createMutationQuery("delete from Promotion where id = :id");
            query.setParameter("id", promotionId);
            query.executeUpdate();
            transaction.commit();
            logger.debug("Deleted promotion id={}", promotionId);
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Failed to delete promotion {}", promotionId, e);
            throw new ApplicationException("Failed to delete promotion");
        }
    }
}
