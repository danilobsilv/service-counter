package com.api.serviceCounter.deskQueue.dtos;

import com.api.serviceCounter.deskQueue.QueueCode;

import java.time.Instant;
import java.util.UUID;

public record DeskQueueResponse(
        UUID id,
        UUID serviceDeskId,
        QueueCode code,
        String name,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {}