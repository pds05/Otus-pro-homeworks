package ru.otus.java.pro.mt.core.transfers.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.otus.java.pro.mt.core.transfers.configs.properties.TransfersProperties;
import ru.otus.java.pro.mt.core.transfers.dtos.ExecuteTransferDtoRq;
import ru.otus.java.pro.mt.core.transfers.entities.Transfer;
import ru.otus.java.pro.mt.core.transfers.exceptions_handling.BusinessLogicException;
import ru.otus.java.pro.mt.core.transfers.metrics.TransferMetricsService;
import ru.otus.java.pro.mt.core.transfers.repositories.TransfersRepository;
import ru.otus.java.pro.mt.core.transfers.validators.TransferRequestValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransfersServiceImpl implements TransfersService {
    private final TransfersRepository transfersRepository;
    private final TransferRequestValidator transferRequestValidator;
    private final TransfersProperties transfersProperties;
    private final LimitsServiceImpl limitsService;
    private final TransferMetricsService transferMetricsService;

    @Override
    public Optional<Transfer> getTransferById(String id, String clientId) {
        return transfersRepository.findByIdAndClientId(id, clientId);
    }

    @Override
    public List<Transfer> getAllTransfers(String clientId, Integer page, Integer offset) {
        return transfersRepository.findAllByClientId(clientId, PageRequest.of(page, offset));
    }

    @Override
    public void execute(String clientId, ExecuteTransferDtoRq executeTransferDtoRq) {
        transferMetricsService.incrementTotalTransfersCounter();
        try {
            transferRequestValidator.validate(executeTransferDtoRq);
            if (executeTransferDtoRq.getAmount().compareTo(transfersProperties.getMaxTransferSum()) > 0) {
                log.warn("Transfer amount exceeds the allowed amount, clientId={}, amount={}, maxTransferSum={}", clientId, executeTransferDtoRq.getAmount(), transfersProperties.getMaxTransferSum());
                throw new BusinessLogicException("Transfer amount exceeds the allowed amount", "AMOUNT_TRANSFER_LIMIT");
            }
            // execution
            if (!limitsService.isLimitEnough(clientId, executeTransferDtoRq.getSourceAccount(), executeTransferDtoRq.getAmount())) {
                log.warn("Not enough funds in the source account, clientId={}, accountId={} amount={}", clientId, executeTransferDtoRq.getSourceAccount(), executeTransferDtoRq.getAmount());
                throw new BusinessLogicException("Not enough funds in the source account", "LIMIT_NOT_ENOUGH");
            }
            Transfer transfer = new Transfer(
                    UUID.randomUUID().toString(),
                    clientId, executeTransferDtoRq.getTargetClientId(),
                    executeTransferDtoRq.getSourceAccount(),
                    executeTransferDtoRq.getTargetAccount(),
                    executeTransferDtoRq.getMessage(),
                    executeTransferDtoRq.getAmount());
            save(transfer);
            transferMetricsService.incrementSuccessTransfersCounter();
        } catch (Exception e) {
            transferMetricsService.incrementFailedTransfersCounter();
            throw e;
        }
    }

    @Override
    public void save(Transfer transfer) {
        transfersRepository.save(transfer);
    }
}
