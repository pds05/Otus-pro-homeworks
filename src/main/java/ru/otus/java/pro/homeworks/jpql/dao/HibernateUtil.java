package ru.otus.java.pro.homeworks.jpql.dao;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUtil {
    public static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            logger.info("Creating new session factory");
            sessionFactory = new Configuration()
                    .configure()
                    .buildSessionFactory();
            logger.info("Session factory created");
        }
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null) {
            logger.info("Closing session factory");
            sessionFactory.close();
            logger.info("Session factory closed");
        }
    }
}
