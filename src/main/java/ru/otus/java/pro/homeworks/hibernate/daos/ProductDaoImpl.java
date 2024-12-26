package ru.otus.java.pro.homeworks.hibernate.daos;

import jakarta.persistence.EntityGraph;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.homeworks.hibernate.entities.Product;
import ru.otus.java.pro.homeworks.hibernate.entities.ProductType;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductDaoImpl implements ProductDao {
    public static final Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

    private final SessionFactory sessionFactory;

    @Override
    public Optional<Product> findById(long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        EntityGraph<?> graph = session.createEntityGraph("Product.promotions");
        Optional<Product> product = session.createQuery("from Product where id = :id", Product.class)
                .setParameter("id", id)
                .setHint("jakarta.persistence.fetchgraph", graph)
                .uniqueResultOptional();
        session.getTransaction().commit();
        return product;
    }

    @Override
    public List<Product> findByProductType(String productType) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        RootGraph<Product> graph = session.createEntityGraph(Product.class, "Product.promotions");
        List<Product> products = session.createNamedQuery("Product.findByProductTypeTitle", Product.class)
                .setParameter("title", productType)
                .applyFetchGraph(graph)
                .list();
        session.getTransaction().commit();
        return products;
    }

    @Override
    public Optional<Product> findByTitle(String title) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional<Product> product = session.createNamedQuery("Product.findByTitleWithData", Product.class)
                .setParameter("title", "%" + title + "%")
                .uniqueResultOptional();
        session.getTransaction().commit();
        return product;
    }

    @Override
    public List<ProductType> findProductTypes() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<ProductType> productTypes = session.createQuery("from ProductType", ProductType.class)
                .list();
        session.getTransaction().commit();
        return productTypes;
    }

    @Override
    public Optional<ProductType> findProductTypeByTitle(String title) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Optional<ProductType> productType = session.createNamedQuery("ProductType.findByTitleWithData", ProductType.class)
                .setParameter("title", title)
                .uniqueResultOptional();
        session.getTransaction().commit();
        return productType;
    }

    @Override
    public List<Product> findAll() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Product> products = session.createNamedQuery("Product.findAllWithData", Product.class)
                .getResultList();
        session.getTransaction().commit();
        return products;
    }

    @Override
    public Product save(Product product) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.persist(product);
            session.getTransaction().commit();
            logger.debug("Saved product {}", product);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to save product {}", product, e);
            throw new ApplicationException("Failed to save product");
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            Product mergedProduct = session.merge(product);
            session.getTransaction().commit();
            logger.debug("Updated product {}", mergedProduct);
            return mergedProduct;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to update product {}", product, e);
            throw new ApplicationException("Failed to update product");
        }
    }

    @Override
    public void delete(long productId) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            MutationQuery query = session.createMutationQuery("delete from Product where id = :id");
            query.setParameter("id", productId);
            query.executeUpdate();
            session.getTransaction().commit();
            logger.debug("Deleted product id={}", productId);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Failed to delete product id={}", productId, e);
            throw new ApplicationException("Failed to delete product");
        }
    }
}
