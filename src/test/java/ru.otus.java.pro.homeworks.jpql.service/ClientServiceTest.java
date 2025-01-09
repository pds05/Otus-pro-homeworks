package ru.otus.java.pro.homeworks.jpql.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.java.pro.homeworks.jpql.dao.ClientDao;
import ru.otus.java.pro.homeworks.jpql.dao.HibernateUtil;
import ru.otus.java.pro.homeworks.jpql.entity.Client;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServiceTest {
    private static ClientService clientService;

    @BeforeAll
    public static void init() {
        ClientDao dao = new ClientDao(HibernateUtil.getSessionFactory());
        clientService = new ClientService(dao);
    }

    @Test
    public void createTestClient() {
        clientService.register("Вася", "Ломоносова", "79998880080");
        clientService.register("Коля", "Гагарина", "79997770070", "79997770071");
        clientService.register("Миша", "Победы", "79991112220", "79991112221", "79991112223");
        assertEquals(3, clientService.getAllClients().size());

        Client client = clientService.getClient(1);
        assertNotNull(client);
        System.out.println(client);

        assertEquals("Вася", client.getName());
        assertEquals("Ломоносова", client.getAddress().getStreet());
        assertEquals(1, client.getPhones().size());
        assertTrue(client.getPhones().stream().anyMatch(phone -> phone.getNumber().equals("79998880080")));

        client = clientService.getClient(2);
        assertNotNull(client);
        System.out.println(client);

        assertEquals("Коля", client.getName());
        assertEquals("Гагарина", client.getAddress().getStreet());
        assertEquals(2, client.getPhones().size());
        assertTrue(client.getPhones().stream().anyMatch(phone -> phone.getNumber().equals("79997770071")));

        client = clientService.getClient(3);
        assertNotNull(client);
        System.out.println(client);

        assertEquals("Миша", client.getName());
        assertEquals("Победы", client.getAddress().getStreet());
        assertEquals(3, client.getPhones().size());
        assertTrue(client.getPhones().stream().anyMatch(phone -> phone.getNumber().equals("79991112221")));
    }

    @AfterAll
    public static void destroy() {
        HibernateUtil.closeSessionFactory();
    }
}
