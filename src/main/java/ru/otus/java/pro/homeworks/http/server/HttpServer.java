package ru.otus.java.pro.homeworks.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private Properties properties;
    private ExecutorService executorService;
    private boolean active = true;
    private Socket socket;
    private ServerSocket serverSocket;

    public HttpServer(Properties properties) {
        this.properties = properties;
    }

    public void start() {
        int port = Integer.parseInt(properties.getProperty("http_port"));
        int handlers = Integer.parseInt(properties.getProperty("request_handlers"));
        executorService = Executors.newFixedThreadPool(handlers);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            System.out.println("Сервер запущен на порту: " + port);
            do {
                Socket socket = serverSocket.accept();
                this.socket = socket;
                Thread request = new Thread(() -> {
                    try {
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();
                        byte[] buffer = new byte[8192];
                        int n = inputStream.read(buffer);
                        if (n < 1) {
                            return;
                        }
                        String rawRequest = new String(buffer, 0, n);
                        int maxSize = Integer.parseInt(properties.getProperty("request_max_size_mb"));
                        try {
                            HttpRequest httpRequest = new HttpRequest(rawRequest, maxSize);
                            goodResponse(outputStream);
                            System.out.println(httpRequest);
                            if (httpRequest.getUri().equalsIgnoreCase("/shutdown")) {
                                shutdown();
                            }
                        } catch (HttpRequestException e) {
                            System.out.println("Ошибка парсинга запроса rawRequest: " + rawRequest);
                            badResponse(outputStream);
                        }
                    } catch (IOException e) {
                        throw new HttpServerException("Ошибка получения потока ввода/вывода");
                    } finally {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            throw new HttpServerException("Ошибка закрытия сокета");
                        }
                    }
                });
                executorService.execute(request);
            } while (active);
        } catch (SocketException e) {
            System.out.println("Прерывание сервера");
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpServerException("Ошибка запуска сервера");
        }
        System.out.println("Сервер остановлен");
    }

    public void goodResponse(OutputStream out) {
        String response = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>SUCCESS REQUEST</h1></body></html>";
        try {
            out.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            badResponse(out);
        }
    }

    public void badResponse(OutputStream out) {
        String response = "" +
                "HTTP/1.1 500 Internal Server Error\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<html><body><h1>INTERNAL SERVER ERROR</h1></body></html>";
        try {
            out.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpServerException("Ошибка отправки ответа");
        }
    }

    public void shutdown() {
        System.out.println("Получена команда остановки сервера");
        active = false;
        if (executorService != null) {
            executorService.shutdownNow();
        }
        if (socket != null) {
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new HttpServerException("Не удалось закрыть сокет");
            }
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new HttpServerException("Не удалось закрыть сервер сокет");
            }
        }
    }
}
