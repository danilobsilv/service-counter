package com.api.serviceCounter.deskQueue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeskQueueRepository extends JpaRepository<DeskQueue, UUID> {
    Optional<DeskQueue> findByServiceDeskIdAndCode(UUID serviceDeskId, QueueCode code);
}