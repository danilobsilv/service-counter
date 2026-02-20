package com.api.serviceCounter.serviceDesk.dtos;

public record UpdateDeskRequest(
        String name,
        String timezone
) {}