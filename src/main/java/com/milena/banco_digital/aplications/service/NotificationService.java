package com.milena.banco_digital.aplications.service;

import com.milena.banco_digital.domain.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void notify(Transfer transfer) {
        log.info("[NOTIFICAÇÃO] Transferência {} concluída: R$ {} de conta {} para conta {}",
                transfer.getId(),
                transfer.getAmount(),
                transfer.getSource().getId(),
                transfer.getTarget().getId());
    }
}