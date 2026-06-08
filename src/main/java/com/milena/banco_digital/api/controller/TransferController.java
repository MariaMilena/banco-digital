package com.milena.banco_digital.api.controller;

import com.milena.banco_digital.api.extensions.StandardResponse;
import com.milena.banco_digital.aplications.service.TransferService;
import com.milena.banco_digital.domain.dto.TransferRequest;
import com.milena.banco_digital.domain.dto.TransferResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
@Tag(name = "Transferências", description = "Transferência de valores entre contas")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @Operation(summary = "Realizar transferência entre contas")
    public ResponseEntity<StandardResponse<TransferResponse>> transfer(
            @RequestBody @Valid TransferRequest request) {
        return StandardResponse.created(transferService.transfer(request));
    }

    @GetMapping("/statement/{accountId}")
    @Operation(summary = "Consultar extrato de uma conta")
    public ResponseEntity<StandardResponse<List<TransferResponse>>> getStatement(
            @PathVariable UUID accountId) {
        return StandardResponse.success(transferService.getStatement(accountId));
    }
}