package com.api.serviceCounter.serviceDesk.dtos;

public record CreateDeskRequest(
        String name,
        String timezone
) {}