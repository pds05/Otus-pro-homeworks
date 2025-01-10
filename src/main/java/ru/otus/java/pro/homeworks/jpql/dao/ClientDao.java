package ru.otus.java.pro.homeworks.jpql.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.MutationQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.homeworks.jpql.entity.Client;
import ru.otus.java.pro.homeworks.jpql.exception.ApplicationException;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@RequiredArgsConstructor
public class ClientDao implements CrudDao<Client> {
    public static final Logger logger = LoggerFactory.getLogger(ClientDao.class);

    private final SessionFactory sessionFactory;

    @Override
    public Client create(Client client) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            session.persist(client);
            session.getTransaction().commit();
            logger.debug("Client created: {}", client);
            return client;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error creating client: {}", client, e);
            throw new ApplicationException("Error creating client " + client.getName());
        }
    }

    @Override
    public Client update(Client client) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            Client mergedClient = session.merge(client);
            session.getTransaction().commit();
            logger.debug("Client updated: {}", mergedClient);
            return mergedClient;
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error updating client: {}", client, e);
            throw new ApplicationException("Error creating client " +
                    (client.getId() != null ? "id=" + client.getId() : client.getName()));
        }
    }

    @Override
    public Optional<Client> findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        RootGraph<Client> graph = session.createEntityGraph(Client.class, "Client.phones");
        Optional<Client> client = session.byId(Client.class)
                .withFetchGraph(graph)
                .loadOptional(id);
        session.getTransaction().commit();
        return client;
    }

    @Override
    public List<Client> findAll() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Client> clients = session.createQuery("from Client", Client.class).list();
        session.getTransaction().commit();
        return clients;
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            MutationQuery query = session.createMutationQuery("delete from Client where id = :id");
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
            logger.debug("Client deleted id={}", id);
        } catch (Exception e) {
            session.getTransaction().rollback();
            logger.error("Error deleting client id={}", id, e);
            throw new ApplicationException("Failed to delete client id=" + id);
        }
    }

}
