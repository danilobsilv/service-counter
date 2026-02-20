package com.api.serviceCounter.serviceDesk.validations;

import com.api.serviceCounter.serviceDesk.dtos.CreateDeskRequest;
import com.api.serviceCounter.shared.domain.BaseValidator;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class ValidateDeskTimezone implements BaseValidator<CreateDeskRequest> {

    @Override
    public void validate(CreateDeskRequest data) {
        if (data.timezone() == null || data.timezone().isBlank()) {
            return; // opcional
        }
        try {
            ZoneId.of(data.timezone().trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("DESK_TIMEZONE_INVALID");
        }
    }
}