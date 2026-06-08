package com.milena.banco_digital.api.controller;

import com.milena.banco_digital.api.extensions.StandardResponse;
import com.milena.banco_digital.aplications.service.AccountService;
import com.milena.banco_digital.domain.dto.AccountResponse;
import com.milena.banco_digital.domain.dto.CreateAccountRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Contas", description = "Gestão de contas bancárias")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Criar nova conta")
    public ResponseEntity<StandardResponse<AccountResponse>> create(
            @RequestBody @Valid CreateAccountRequest request) {
        return StandardResponse.created(accountService.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta por ID")
    public ResponseEntity<StandardResponse<AccountResponse>> findById(@PathVariable UUID id) {
        return StandardResponse.success(accountService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Listar todas as contas")
    public ResponseEntity<StandardResponse<List<AccountResponse>>> findAll() {
        return StandardResponse.success(accountService.findAll());
    }
}