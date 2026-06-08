package com.milena.banco_digital.domain.dto;

import com.milena.banco_digital.domain.Transfer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransferResponse(
    UUID id,
    UUID sourceId,
    UUID targetId,
    BigDecimal amount,
    String status,
    LocalDateTime createdAt
) {
    public static TransferResponse from(Transfer transfer) {
        return new TransferResponse(
            transfer.getId(),
            transfer.getSource().getId(),
            transfer.getTarget().getId(),
            transfer.getAmount(),
            transfer.getStatus(),
            transfer.getCreatedAt()
        );
    }
}