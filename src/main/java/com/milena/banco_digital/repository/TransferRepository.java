package com.milena.banco_digital.repository;

import com.milena.banco_digital.domain.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    List<Transfer> findBySourceIdOrTargetIdOrderByCreatedAtDesc(UUID sourceId, UUID targetId);
}