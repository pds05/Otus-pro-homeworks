package ru.otus.java.pro.spring.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.java.pro.spring.app.entities.Transfer;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransfersRepository extends JpaRepository<Transfer, String> {

    @Query(value = "select t from Transfer t where t.id = :id and (t.clientId = :clientId or t.targetClientId = : clientId)")
    Optional<Transfer> findByIdAndClientId(@Param(value = "id") String id, @Param(value = "clientId") String clientId);

    @Query(value = "select t from Transfer t where t.clientId = :clientId or t.targetClientId = : clientId")
    List<Transfer> findAllByClientId(String clientId);
}
