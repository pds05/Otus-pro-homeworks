package ru.otus.java.pro.spring.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.pro.spring.app.dtos.AccountDto;
import ru.otus.java.pro.spring.app.entities.Account;
import ru.otus.java.pro.spring.app.exceptions_handling.ResourceNotFoundException;
import ru.otus.java.pro.spring.app.services.AccountsService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountsController {
    private final AccountsService accountsService;

    public static final Function<Account, AccountDto> ENTITY_TO_DTO = a -> new AccountDto(a.getId(), a.getAccount(), a.getClientId(), a.getFunds(), a.isBlocked());

    @GetMapping
    public List<AccountDto> getAllAccounts(@RequestHeader(name = "client-id") String clientId) {
        return
                accountsService.getAllAccounts(clientId)
                        .stream()
                        .map(ENTITY_TO_DTO)
                        .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AccountDto getAccountById(@RequestHeader(name = "client-id") String clientId, @PathVariable Integer id) {
        return ENTITY_TO_DTO.apply(accountsService.getAccount(id, clientId).orElseThrow(() -> new ResourceNotFoundException("Счет не найден")));
    }
}
