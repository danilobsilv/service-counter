package com.api.serviceCounter.shared.errors;

import java.time.Instant;

public record ExceptionApi(
        Instant timestamp,
        int httpStatus,
        ErrorTypes errorTypes,
        String errorMessage,
        String path,
        String requestId
) {
}
