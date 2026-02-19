package com.api.serviceCounter.deskQueue.validations;

import com.api.serviceCounter.deskQueue.dtos.CreateDeskQueueRequest;
import com.api.serviceCounter.shared.domain.BaseValidator;
import org.springframework.stereotype.Component;

@Component
public class ValidateDeskQueueCode implements BaseValidator<CreateDeskQueueRequest> {

    @Override
    public void validate(CreateDeskQueueRequest data) {
        if (data.code() == null) {
            throw new IllegalArgumentException("QUEUE_CODE_REQUIRED");
        }
    }
}