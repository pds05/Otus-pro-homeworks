package ru.otus.java.pro.spring.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.pro.spring.app.dtos.ExecuteTransferDtoRq;
import ru.otus.java.pro.spring.app.entities.Account;
import ru.otus.java.pro.spring.app.entities.Transfer;
import ru.otus.java.pro.spring.app.exceptions_handling.BusinessLogicException;
import ru.otus.java.pro.spring.app.exceptions_handling.ValidationException;
import ru.otus.java.pro.spring.app.exceptions_handling.ValidationFieldError;
import ru.otus.java.pro.spring.app.repositories.TransfersRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransfersService {
    private final TransfersRepository transfersRepository;
    private final AccountsService accountsService;

    public Optional<Transfer> getTransferById(String id, String clientId) {
        return transfersRepository.findByIdAndClientId(id, clientId);
    }

    public List<Transfer> getAllTransfers(String clientId) {
        return transfersRepository.findAllByClientId(clientId);
    }

    @Transactional
    public Transfer execute(String clientId, ExecuteTransferDtoRq executeTransferDtoRq) {
        validateExecuteTransferDtoRq(executeTransferDtoRq);
        Account sourceAccount = accountsService.getAccount(executeTransferDtoRq.sourceAccount(), clientId)
                .orElseThrow(() -> new BusinessLogicException("Счет отправителя не существует", "SOURCE_ACCOUNT_NOT_FOUND"));
        BigDecimal amount = new BigDecimal(executeTransferDtoRq.amount());
        if (sourceAccount.getFunds().compareTo(amount) < 0) {
            throw new BusinessLogicException("На счете отправителя не достаточно средств для выполнения перевода", "NOT_ENOUGH_FUNDS");
        }
        if (sourceAccount.isBlocked()) {
            throw new BusinessLogicException("Счет отправителя заблокирован", "SOURCE_ACCOUNT_BLOCKED");
        }
        Account targetAccount = accountsService.getAccount(executeTransferDtoRq.targetAccount(), executeTransferDtoRq.targetClientId())
                .orElseThrow(() -> new BusinessLogicException("Счет получателя не существует", "TARGET_ACCOUNT_NOT_FOUND"));
        if (targetAccount.isBlocked()) {
            throw new BusinessLogicException("Счет получателя заблокирован", "TARGET_ACCOUNT_BLOCKED");
        }
        if (sourceAccount.equals(targetAccount)) {
            throw new BusinessLogicException("Счетом получателя не может быть счет отправителя", "SAME_SOURCE_AND_TARGET_ACCOUNT");
        }
        Transfer transfer = Transfer.builder()
                .id(UUID.randomUUID().toString())
                .client(sourceAccount.getClient())
                .clientId(clientId)
                .targetClientId(targetAccount.getClient().getId())
                .sourceAccount(sourceAccount.getAccount())
                .targetAccount(targetAccount.getAccount())
                .message(executeTransferDtoRq.message())
                .amount(executeTransferDtoRq.amount())
                .build();
        transfersRepository.save(transfer);
        sourceAccount.setFunds(sourceAccount.getFunds().subtract(amount));
        accountsService.saveAccount(sourceAccount);
        targetAccount.setFunds(targetAccount.getFunds().add(amount));
        accountsService.saveAccount(targetAccount);
        log.debug("Create transfer: {}", transfer);
        return transfer;
    }

    private void validateExecuteTransferDtoRq(ExecuteTransferDtoRq executeTransferDtoRq) {
        List<ValidationFieldError> errors = new ArrayList<>();
        if (executeTransferDtoRq.sourceAccount() == null) {
            errors.add(new ValidationFieldError("sourceAccount", "Отсутствует поле в запросе"));
        } else if (executeTransferDtoRq.sourceAccount().length() != 12) {
            errors.add(new ValidationFieldError("sourceAccount", "Длина поля счет отправителя должна составлять 12 символов"));
        } else if (!executeTransferDtoRq.sourceAccount().matches("\\d{12}")) {
            errors.add(new ValidationFieldError("sourceAccount", "Счет отправителя должен состоять из 12 цифр"));
        }

        if (executeTransferDtoRq.targetClientId() == null) {
            errors.add(new ValidationFieldError("targetClientId", "Отсутствует поле в запросе"));
        } else if (executeTransferDtoRq.targetClientId().length() != 10) {
            errors.add(new ValidationFieldError("targetClientId", "Длина номера получателя должна составлять 10 символов"));
        } else if (!executeTransferDtoRq.targetClientId().matches("\\d{10}")) {
            errors.add(new ValidationFieldError("targetClientId", "Номер получателя должен состоять из 10 цифр"));
        }

        if (executeTransferDtoRq.targetAccount() == null) {
            errors.add(new ValidationFieldError("targetAccount", "Отсутствует поле в запросе"));
        } else if (executeTransferDtoRq.targetAccount().length() != 12) {
            errors.add(new ValidationFieldError("targetAccount", "Длина поля счет получателя должна составлять 12 символов"));
        } else if (!executeTransferDtoRq.targetAccount().matches("\\d{12}")) {
            errors.add(new ValidationFieldError("targetAccount", "Счет получателя должен состоять из 12 цифр"));
        }

        if (executeTransferDtoRq.amount() <= 0) {
            errors.add(new ValidationFieldError("amount", "Сумма перевода должна быть больше 0"));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("EXECUTE_TRANSFER_VALIDATION_ERROR", "Проблемы заполнения полей перевода", errors);
        }
    }
}
