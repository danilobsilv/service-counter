package com.api.serviceCounter.deskQueue.dtos;

import com.api.serviceCounter.deskQueue.QueueCode;

public record CreateDeskQueueRequest(
        QueueCode code,
        String name
) {}