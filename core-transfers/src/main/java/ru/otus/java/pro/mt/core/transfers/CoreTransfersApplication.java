package ru.otus.java.pro.mt.core.transfers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CoreTransfersApplication {
	/*
	Домашнее задание:
	1. Создайте отдельный класс для хранение настроек RestClient (url, readTimeout, connectTimeout)
	2. Создайте бин-фабрику, которая по объекту из п.1 позволяет построить объект типа RestClient
	3. При создании RestClient добавьте настройку readTimeout/connectTimeout (пока при создании используется только url)
	4. В RestClientsConfig создавайте RestClient через полученную фабрику
	5. Покройте openapi описанием все контроллеры и дто, которые используете
	 */

	// ----------------------------------------------------
	// TODO Привести пример настройки 2х групп в Swagger
	//

	public static void main(String[] args) {
		SpringApplication.run(CoreTransfersApplication.class, args);
	}
}
