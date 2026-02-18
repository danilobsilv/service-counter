package com.api.serviceCounter.shared.errors;

import com.api.serviceCounter.shared.errors.conflictException.ResourceConflictException;
import com.api.serviceCounter.shared.errors.handleGeneric.HandleGeneric;
import com.api.serviceCounter.shared.errors.notFoundException.ResourceNotFoundException;
import com.api.serviceCounter.shared.errors.validationException.HandleValidation;
import com.api.serviceCounter.shared.logging.RequestIdFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionApi> handleNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ErrorTypes.RESOURCE_NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ExceptionApi> handleConflict(ResourceConflictException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ErrorTypes.RESOURCE_CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(HandleValidation.class)
    public ResponseEntity<ExceptionApi> handleValidation(HandleValidation exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorTypes.UNPROCESSABLE_ENTITY, exception.getMessage(), request);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionApi> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorTypes.UNPROCESSABLE_ENTITY, exception.getMessage(), request);
    }

    // Validação em params/path/etc
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionApi> handleConstraintViolation(ConstraintViolationException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorTypes.UNPROCESSABLE_ENTITY, exception.getMessage(), request);
    }

    @ExceptionHandler(HandleGeneric.class)
    public ResponseEntity<ExceptionApi> handleGeneric(HandleGeneric exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorTypes.INTERNAL_SERVER_ERROR, exception.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionApi> handleUnexpected(Exception exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorTypes.INTERNAL_SERVER_ERROR, exception.getMessage(), request);
    }

    private ResponseEntity<ExceptionApi> buildResponse(HttpStatus status, ErrorTypes errorType, String message, HttpServletRequest request) {
        String requestId = MDC.get(RequestIdFilter.MDC_KEY);

        ExceptionApi response = new ExceptionApi(
                Instant.now(),
                status.value(),
                errorType,
                message,
                request.getRequestURI(),
                requestId
        );

        return ResponseEntity.status(status).body(response);
    }
}