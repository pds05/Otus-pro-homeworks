package ru.otus.java.pro.spring.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.pro.spring.app.entities.Account;
import ru.otus.java.pro.spring.app.repositories.AccountsRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Transactional
    public Account saveAccount(Account account) {
        Account savedAccount = accountsRepository.save(account);
        log.debug("{} account {}", account.getId() == null ? "Saved" : "Updated", savedAccount);
        return accountsRepository.save(account);
    }
}
