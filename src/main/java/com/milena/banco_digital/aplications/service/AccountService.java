package com.milena.banco_digital.aplications.service;

import com.milena.banco_digital.api.exception.InsufficientBalanceException;
import com.milena.banco_digital.domain.Account;
import com.milena.banco_digital.domain.dto.AccountResponse;
import com.milena.banco_digital.domain.dto.CreateAccountRequest;
import com.milena.banco_digital.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountResponse create(CreateAccountRequest request) {
        Account account = new Account();
        account.setName(request.name());
        account.setBalance(request.initialBalance());
        return AccountResponse.from(accountRepository.save(account));
    }

    public AccountResponse findById(UUID id) {
        return accountRepository.findById(id)
                .map(AccountResponse::from)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    public List<AccountResponse> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(AccountResponse::from)
                .toList();
    }
}