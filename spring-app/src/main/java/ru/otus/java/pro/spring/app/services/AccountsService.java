package ru.otus.java.pro.spring.app.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.spring.app.entities.Account;
import ru.otus.java.pro.spring.app.repositories.AccountsRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AccountsService {
    private final AccountsRepository accountsRepository;

    public Optional<Account> getAccount(Integer id, String clientId) {
        return accountsRepository.findAccountAndClientByIdAndClientId(id, clientId);
    }

    public Optional<Account> getAccount(String account, String clientId) {
        return accountsRepository.findAccountAndClientByAccountAndClientId(account, clientId);
    }

    public List<Account> getAllAccounts(String clientId) {
        return accountsRepository.findAllByClientId(clientId);
    }

}
