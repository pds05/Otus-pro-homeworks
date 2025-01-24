package ru.otus.java.pro.homeworks.hibernate.services;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.otus.java.pro.homeworks.hibernate.config.SessionFactoryConfigurator;
import ru.otus.java.pro.homeworks.hibernate.daos.OrderDaoImpl;
import ru.otus.java.pro.homeworks.hibernate.daos.UserDao;
import ru.otus.java.pro.homeworks.hibernate.daos.UserDaoImpl;
import ru.otus.java.pro.homeworks.hibernate.dtos.OrderDto;
import ru.otus.java.pro.homeworks.hibernate.dtos.UserProfileDto;
import ru.otus.java.pro.homeworks.hibernate.exceptions.ApplicationException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {
    static SessionFactory sessionFactory;
    static UserDao dao;
    static UserService service;

    @BeforeAll
    public static void init() {
        sessionFactory = SessionFactoryConfigurator.configInstance();
        dao = new UserDaoImpl(sessionFactory);
        service = new UserService(dao, new OrderDaoImpl(sessionFactory));
    }

    @Test
    @Order(1)
    public void testFindAll() {
        assertEquals(3, dao.findAll().size());
    }

    @Test
    @Order(2)
    public void createProfile() {
        UserProfileDto user = service.createProfile("test", "test", "79998887766", "test@test.com");
        assertNotNull(user.getUserId());
        assertEquals(4, dao.findAll().size());
    }

    @Test
    @Order(3)
    public void failedAuthorization() {
        assertThatThrownBy(() -> service.getProfile(123L))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("User must be authenticated");
    }

    @Test
    @Order(4)
    public void login() {
        UserProfileDto user = service.login("test", "test");
        assertNotNull(user);
    }

    @Test
    @Order(5)
    public void restoreAndUpdateProfile() {
        UserProfileDto user1 = service.restoreProfile("79998887766", null);
        UserProfileDto user2 = service.restoreProfile(null, "test@test.com");
        assertEquals(user1, user2);

        user1.setPassword("password");
        user1.setPhone("79991234567");
        service.updateProfile(user1);

        user2 = service.getProfile(user1.getUserId());
        assertEquals(user1, user2);
        assertEquals("password", user2.getPassword());
        assertEquals("79991234567", user2.getPhone());
    }

    @Test
    @Order(6)
    public void deleteProfile() {
        UserProfileDto user = service.login("test", "password");
        service.deleteProfile(user.getUserId());

        assertThatThrownBy(() -> service.login("test", "password"))
                .isInstanceOf(ApplicationException.class)
                .hasMessageContaining("Wrong username or password");
    }

    @Test
    public void removeOrder() {
        UserProfileDto user = service.login("Коля", "321");
        List<OrderDto> orders = service.getOrders(user.getUserId());
        if (!orders.isEmpty()) {
            OrderDto order = orders.get(0);
            service.removeOrder(user.getUserId(), order.getOrderId());
            List<OrderDto> updatedOrders = service.getOrders(user.getUserId());
            assertFalse(updatedOrders.stream().anyMatch(o -> o.getOrderId() == order.getOrderId()));
        }
    }

    @AfterAll
    public static void close() {
        System.out.println("Opened sessions = " + sessionFactory.getStatistics().getSessionOpenCount());
        System.out.println("Closed sessions = " + sessionFactory.getStatistics().getSessionCloseCount());
        sessionFactory.close();
    }
}
