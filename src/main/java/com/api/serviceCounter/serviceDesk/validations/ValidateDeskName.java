package com.api.serviceCounter.serviceDesk.validations;

import com.api.serviceCounter.serviceDesk.dtos.CreateDeskRequest;
import com.api.serviceCounter.shared.domain.BaseValidator;
import org.springframework.stereotype.Component;

@Component
public class ValidateDeskName implements BaseValidator<CreateDeskRequest> {

    @Override
    public void validate(CreateDeskRequest data) {
        if (data.name() == null || data.name().isBlank()) {
            throw new IllegalArgumentException("DESK_NAME_REQUIRED");
        }
        var n = data.name().trim();
        if (n.length() < 3 || n.length() > 120) {
            throw new IllegalArgumentException("DESK_NAME_INVALID_LENGTH");
        }
    }
}