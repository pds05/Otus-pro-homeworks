package ru.otus.java.pro.homeworks.hibernate.config;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionFactoryConfigurator {
    public static final Logger logger = LoggerFactory.getLogger(SessionFactoryConfigurator.class);
    public static final String ENTITIES_PACKAGE = "ru.otus.java.pro.homeworks.hibernate.entities";
    private static SessionFactory sessionFactory;

    public static SessionFactory configInstance() {
        if (sessionFactory == null) {
            logger.info("Creating session factory");
            Configuration configuration = new Configuration();
            fillEntities(configuration);
            sessionFactory = configuration.configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            sessionFactory.getStatistics().setStatisticsEnabled(true);
            logger.info("Session factory created");
        }
        return sessionFactory;
    }

    private static void fillEntities(Configuration config) {
        String dir = ENTITIES_PACKAGE.replace('.', '/');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> resources = classLoader.getResources(dir);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                File directory = new File(resource.getFile());
                Arrays.stream(directory.listFiles(pathname -> pathname.isFile() && pathname.getName().endsWith(".class")))
                        .map(File::getName)
                        .map(fileName -> {
                            try {
                                Class<?> cls = Class.forName(ENTITIES_PACKAGE + "." + fileName.replace(".class", ""));
                                return cls.isAnnotationPresent(Entity.class) ? cls : null;
                            } catch (ClassNotFoundException e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .forEach(config::addAnnotatedClass);
            }
        } catch (IOException e) {
            throw new ApplicationException("Entities reading error");
        }
    }
}
