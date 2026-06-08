package com.milena.banco_digital.application.service;

import com.milena.banco_digital.api.exception.InsufficientBalanceException;
import com.milena.banco_digital.aplications.service.NotificationService;
import com.milena.banco_digital.aplications.service.TransferService;
import com.milena.banco_digital.domain.Account;
import com.milena.banco_digital.domain.Transfer;
import com.milena.banco_digital.domain.dto.TransferRequest;
import com.milena.banco_digital.repository.AccountRepository;
import com.milena.banco_digital.repository.TransferRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock AccountRepository accountRepository;
    @Mock TransferRepository transferRepository;
    @Mock NotificationService notificationService;
    @InjectMocks TransferService transferService;

    @Test
    void deveLancarExcecaoQuandoSaldoInsuficiente() {
        UUID sourceId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();

        Account source = new Account(sourceId, "Ana", new BigDecimal("50.00"), null);
        Account target = new Account(targetId, "João", new BigDecimal("0.00"), null);

        when(accountRepository.findById(any())).thenAnswer(inv -> {
            UUID id = inv.getArgument(0);
            if (id.equals(sourceId)) return Optional.of(source);
            if (id.equals(targetId)) return Optional.of(target);
            return Optional.empty();
        });

        TransferRequest request = new TransferRequest(sourceId, targetId, new BigDecimal("200.00"));

        assertThrows(InsufficientBalanceException.class, () -> transferService.transfer(request));
        verify(transferRepository, never()).save(any());
    }

    @Test
    void deveRealizarTransferenciaComSucesso() {
        UUID sourceId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();

        Account source = new Account(sourceId, "Ana", new BigDecimal("500.00"), null);
        Account target = new Account(targetId, "João", new BigDecimal("100.00"), null);

        when(accountRepository.findById(any())).thenAnswer(inv -> {
            UUID id = inv.getArgument(0);
            if (id.equals(sourceId)) return Optional.of(source);
            if (id.equals(targetId)) return Optional.of(target);
            return Optional.empty();
        });

        Transfer transferSalva = new Transfer(UUID.randomUUID(), source, target,
                new BigDecimal("200.00"), "COMPLETED", null);
        when(transferRepository.save(any())).thenReturn(transferSalva);

        TransferRequest request = new TransferRequest(sourceId, targetId, new BigDecimal("200.00"));
        var response = transferService.transfer(request);

        assertEquals("COMPLETED", response.status());
        assertEquals(new BigDecimal("300.00"), source.getBalance());
        assertEquals(new BigDecimal("300.00"), target.getBalance());
        verify(notificationService).notify(any());
    }

    @Test
    void deveLancarExcecaoQuandoContaOrigemNaoExiste() {
        UUID sourceId = UUID.randomUUID();
        UUID targetId = UUID.randomUUID();

        when(accountRepository.findById(any())).thenReturn(Optional.empty());

        TransferRequest request = new TransferRequest(sourceId, targetId, new BigDecimal("100.00"));

        assertThrows(EntityNotFoundException.class, () -> transferService.transfer(request));
        verify(transferRepository, never()).save(any());
    }

    @Test
    void naoDeveTransferirParaMesmaConta() {
        UUID mesmoId = UUID.randomUUID();
        TransferRequest request = new TransferRequest(mesmoId, mesmoId, new BigDecimal("100.00"));

        assertThrows(IllegalArgumentException.class, () -> transferService.transfer(request));
        verify(accountRepository, never()).findById(any());
        verify(transferRepository, never()).save(any());
    }
}