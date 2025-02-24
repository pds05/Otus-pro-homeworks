package ru.otus.java.pro.mt.core.transfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreTransfersApplication {
	/*
	Домашнее задание:
	1. Изучить текущее состояние проекта
	2. В dev/environment добавьте docker-compose с поднятием Kafka
	3. После успешного исполнения перевода отправьте в кафку (в топик: "mt.transfers.status.info") сообщение вида:
	  {
	    "transferId": "..",
	    "status": "EXECUTED"
	  }
	4. Сделайте  "мини-сервис" нотификаций, который вычитывет топик кафки из п. 4 и выводит в лог сообщение
	"По переводу ${id} клиенту отправлена нотификация"
	 */

	public static void main(String[] args) {
		SpringApplication.run(CoreTransfersApplication.class, args);
	}
}
