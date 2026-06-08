package com.milena.banco_digital.domain.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateAccountRequest(

    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotNull(message = "Saldo inicial é obrigatório")
    @DecimalMin(value = "0.0", message = "Saldo inicial não pode ser negativo")
    BigDecimal initialBalance
) {}