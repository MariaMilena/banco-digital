package com.milena.banco_digital.domain.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(

    @NotNull(message = "Conta de origem é obrigatória")
    UUID sourceId,

    @NotNull(message = "Conta de destino é obrigatória")
    UUID targetId,

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor mínimo de transferência é R$ 0,01")
    BigDecimal amount
) {}
