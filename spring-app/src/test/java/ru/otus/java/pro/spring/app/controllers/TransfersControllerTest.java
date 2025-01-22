package ru.otus.java.pro.spring.app.controllers;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.java.pro.spring.app.SpringAppApplication;
import ru.otus.java.pro.spring.app.dtos.AccountDto;
import ru.otus.java.pro.spring.app.dtos.ExecuteTransferDtoRq;
import ru.otus.java.pro.spring.app.dtos.TransferDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringAppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class TransfersControllerTest {
    public static final String CLIENT_ID = "1000000001";
    public static final String SOURCE_ACCOUNT = "880979287826";
    public static final String DESTINATION_ACCOUNT = "757189807296";
    private static BigDecimal beginFundsSourceAccount;
    private static BigDecimal beginFundsDestinationAccount;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Order(1)
    @Test
    public void getBeginsClientsFunds() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client-id", CLIENT_ID);
        HttpEntity<ExecuteTransferDtoRq> httpEntity = new HttpEntity<>(httpHeaders);
        String url = createUrl("/api/v1/accounts");
        ResponseEntity<AccountDto[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AccountDto[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        beginFundsSourceAccount = Arrays.stream(response.getBody())
                .filter(a -> a.account().equals(SOURCE_ACCOUNT))
                .findFirst()
                .orElseThrow().funds();
        beginFundsDestinationAccount = Arrays.stream(response.getBody())
                .filter(a -> a.account().equals(DESTINATION_ACCOUNT))
                .findFirst()
                .orElseThrow().funds();
        assertNotNull(beginFundsSourceAccount);
        assertNotNull(beginFundsDestinationAccount);
        assertNotEquals(BigDecimal.ZERO, beginFundsSourceAccount);
        assertNotEquals(BigDecimal.ZERO, beginFundsDestinationAccount);
    }

    @Order(2)
    @Test
    public void executeTransferTest() {
        ExecuteTransferDtoRq transferDto = new ExecuteTransferDtoRq(
                CLIENT_ID,
                SOURCE_ACCOUNT,
                DESTINATION_ACCOUNT,
                "Перевод между счетами одного клиента",
                10);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client-id", CLIENT_ID);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExecuteTransferDtoRq> httpEntity = new HttpEntity<>(transferDto, httpHeaders);
        String url = createUrl("/api/v1/transfers");
        ResponseEntity<TransferDto> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, TransferDto.class);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(36, response.getBody().id().length());
    }

    @Order(3)
    @Test
    public void getClientsFundsAfterTransfer() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("client-id", CLIENT_ID);
        HttpEntity<ExecuteTransferDtoRq> httpEntity = new HttpEntity<>(httpHeaders);
        String url = createUrl("/api/v1/accounts");
        ResponseEntity<AccountDto[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, AccountDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        BigDecimal finalFundsSourceAccount = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .filter(a -> a.account().equals(SOURCE_ACCOUNT))
                .findFirst()
                .orElseThrow().funds();
        BigDecimal finalFundsDestinationAccount = Arrays.stream(response.getBody())
                .filter(a -> a.account().equals(DESTINATION_ACCOUNT))
                .findFirst()
                .orElseThrow().funds();

        assertEquals(0, beginFundsSourceAccount.subtract(finalFundsSourceAccount).compareTo(BigDecimal.TEN));
        assertEquals(0, finalFundsDestinationAccount.subtract(beginFundsDestinationAccount).compareTo(BigDecimal.TEN));
    }

    private String createUrl(String path) {
        return "http://localhost:" + port + path;
    }


}
