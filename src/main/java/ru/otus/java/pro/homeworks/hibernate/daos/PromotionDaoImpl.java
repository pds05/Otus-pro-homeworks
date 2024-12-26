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
        session.beginTransaction();
        EntityGraph<?> graph = session.createEntityGraph("Promotion.products");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", graph);
        Promotion promotion = session.find(Promotion.class, id, properties);
        session.getTransaction().commit();
        return Optional.ofNullable(promotion);
    }

    @Override
    public List<Promotion> findAll() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        RootGraph<Promotion> graph = session.createEntityGraph(Promotion.class, "Promotion.products");
        List<Promotion> promotions = session.createQuery("from Promotion", Promotion.class)
                .applyFetchGraph(graph)
                .list();
        session.getTransaction().commit();
        return promotions;
    }

    @Override
    public Promotion save(Promotion promotion) {
        Session session = sessionFactory.getCurrentSession();
        Promotion savedPromotion;
        try {
            session.beginTransaction();
            session.persist(promotion);
            session.getTransaction().commit();
            logger.debug("Saved promotion {}", promotion);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to save promotion {}", promotion, e);
            throw new ApplicationException("Failed to save promotion");
        }
        return promotion;
    }

    @Override
    public Promotion update(Promotion promotion) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            Promotion mergedPromotion = session.merge(promotion);
            session.getTransaction().commit();
            logger.debug("Updated promotion {}", mergedPromotion);
            return mergedPromotion;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to save promotion {}", promotion, e);
            throw new ApplicationException("Failed to save promotion");
        }
    }

    @Override
    public void delete(long promotionId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            MutationQuery query = session.createMutationQuery("delete from Promotion where id = :id");
            query.setParameter("id", promotionId);
            query.executeUpdate();
            session.getTransaction().commit();
            logger.debug("Deleted promotion id={}", promotionId);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to delete promotion {}", promotionId, e);
            throw new ApplicationException("Failed to delete promotion");
        }
    }
}
