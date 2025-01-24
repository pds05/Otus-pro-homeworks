package ru.otus.java.pro.spring.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.java.pro.spring.app.entities.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Integer> {

    List<Account> findAllByClientId(String clientId);

    Optional<Account> findAccountAndClientByIdAndClientId(Integer id, String clientId);

    Optional<Account> findAccountAndClientByAccountAndClientId(String account, String clientId);
}
