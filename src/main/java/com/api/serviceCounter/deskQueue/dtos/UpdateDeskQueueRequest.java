package com.api.serviceCounter.deskQueue.dtos;

public record UpdateDeskQueueRequest(
        String name,
        Boolean isActive
) {}