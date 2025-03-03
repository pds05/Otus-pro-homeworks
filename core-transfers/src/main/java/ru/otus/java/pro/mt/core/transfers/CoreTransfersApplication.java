package ru.otus.java.pro.mt.core.transfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreTransfersApplication {
    /*
    Домашнее задание:
    - Достройте dev/environment таким образом, чтобы через docker-compose запускались все сервисы,
    и все необходимое окружение (БД, ELK, Prometheus/Grafana)
    - Добавьте в мс переводов 3 метрики: Получено запросов на выолнение перевода, Кол-во успешных переводов,
    количество неуспешных переводов
    - Добавьте пагинацию к запросу списка переводов, чтобы при запросе можно указать размер
    (по-умолчанию 20, максимально 1000) и номер страницы
    */
    public static void main(String[] args) {
        SpringApplication.run(CoreTransfersApplication.class, args);
    }
}
