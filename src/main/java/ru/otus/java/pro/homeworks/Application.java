package ru.otus.java.pro.homeworks;

import ru.otus.java.pro.homeworks.http.server.HttpServer;
import ru.otus.java.pro.homeworks.http.server.HttpServerException;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public class Application {
    private static final String RESOURCE_QUERIES_FILE = "server.properties";

    public static void main(String[] args) {
        HttpServer server = new HttpServer(loadProperties());
        try {
            server.start();
        } catch (HttpServerException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        Optional<URL> resource = Optional.ofNullable(Application.class.getClassLoader().getResource(RESOURCE_QUERIES_FILE));
        resource.ifPresent(resourceUrl -> {
            try (BufferedReader reader = Files.newBufferedReader(Path.of(resourceUrl.toURI()))) {
                properties.load(reader);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                throw new RuntimeException("Не удалось прочитать файл \\resources\\" + RESOURCE_QUERIES_FILE);
            }
            System.out.println("Настройки сервера заружены из файла \\resources\\" + RESOURCE_QUERIES_FILE + ": " + properties);
        });
        return properties;
    }
}
