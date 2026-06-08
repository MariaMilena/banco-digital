package com.milena.banco_digital.aplications.service;

import com.milena.banco_digital.api.exception.InsufficientBalanceException;
import com.milena.banco_digital.domain.Account;
import com.milena.banco_digital.domain.Transfer;
import com.milena.banco_digital.domain.dto.TransferRequest;
import com.milena.banco_digital.domain.dto.TransferResponse;
import com.milena.banco_digital.repository.AccountRepository;
import com.milena.banco_digital.repository.TransferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final NotificationService notificationService;

    @Transactional
    public TransferResponse transfer(TransferRequest request) {

        // Ordena os IDs para sempre bloquear na mesma ordem — evita deadlock em concorrência
        UUID firstId  = request.sourceId().compareTo(request.targetId()) < 0
                ? request.sourceId() : request.targetId();
        UUID secondId = request.sourceId().compareTo(request.targetId()) < 0
                ? request.targetId() : request.sourceId();

        Account first  = accountRepository.findById(firstId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada: " + firstId));
        Account second = accountRepository.findById(secondId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada: " + secondId));

        Account source = first.getId().equals(request.sourceId()) ? first : second;
        Account target = first.getId().equals(request.targetId()) ? first : second;

        if (source.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException("Saldo insuficiente para realizar a transferência");
        }

        source.setBalance(source.getBalance().subtract(request.amount()));
        target.setBalance(target.getBalance().add(request.amount()));

        accountRepository.save(source);
        accountRepository.save(target);

        Transfer transfer = new Transfer();
        transfer.setSource(source);
        transfer.setTarget(target);
        transfer.setAmount(request.amount());
        transfer.setStatus("COMPLETED");
        transfer.setCreatedAt(LocalDateTime.now());

        Transfer saved = transferRepository.save(transfer);
        notificationService.notify(saved);

        return TransferResponse.from(saved);
    }

    public List<TransferResponse> getStatement(UUID accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        return transferRepository
                .findBySourceIdOrTargetIdOrderByCreatedAtDesc(accountId, accountId)
                .stream()
                .map(TransferResponse::from)
                .toList();
    }
}