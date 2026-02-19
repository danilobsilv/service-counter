package com.api.serviceCounter.serviceDesk.dtos;

import java.time.Instant;
import java.util.UUID;

public record DeskResponse(
        UUID id,
        String name,
        String timezone,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt
) {}