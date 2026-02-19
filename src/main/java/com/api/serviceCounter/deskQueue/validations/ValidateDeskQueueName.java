package com.api.serviceCounter.deskQueue.validations;

import com.api.serviceCounter.deskQueue.dtos.CreateDeskQueueRequest;
import com.api.serviceCounter.shared.domain.BaseValidator;
import org.springframework.stereotype.Component;

@Component
public class ValidateDeskQueueName implements BaseValidator<CreateDeskQueueRequest> {

    @Override
    public void validate(CreateDeskQueueRequest data) {
        if (data.name() == null || data.name().isBlank()) {
            throw new IllegalArgumentException("QUEUE_NAME_REQUIRED");
        }
        var n = data.name().trim();
        if (n.length() < 3 || n.length() > 120) {
            throw new IllegalArgumentException("QUEUE_NAME_INVALID_LENGTH");
        }
    }
}