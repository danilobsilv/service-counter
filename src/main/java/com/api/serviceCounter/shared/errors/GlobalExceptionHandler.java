package com.api.serviceCounter.shared.errors;

import com.api.serviceCounter.shared.errors.conflictException.ResourceConflictException;
import com.api.serviceCounter.shared.errors.handleGeneric.HandleGeneric;
import com.api.serviceCounter.shared.errors.notFoundException.ResourceNotFoundException;
import com.api.serviceCounter.shared.errors.validationException.HandleValidation;
import com.api.serviceCounter.shared.logging.RequestIdFilter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionApi> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ErrorTypes.RESOURCE_NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionApi> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ErrorTypes.RESOURCE_NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<ExceptionApi> handleConflict(ResourceConflictException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ErrorTypes.RESOURCE_CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionApi> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        String msg = resolveConstraintMessage(ex);
        return buildResponse(HttpStatus.CONFLICT, ErrorTypes.RESOURCE_CONFLICT, msg, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionApi> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ErrorTypes.RESOURCE_CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(HandleValidation.class)
    public ResponseEntity<ExceptionApi> handleValidation(HandleValidation ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorTypes.UNPROCESSABLE_ENTITY, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionApi> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorTypes.UNPROCESSABLE_ENTITY, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionApi> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + Objects.toString(fe.getDefaultMessage(), "invalid"))
                .distinct()
                .collect(Collectors.joining("; "));

        if (message.isBlank()) {
            message = ex.getMessage();
        }

        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorTypes.UNPROCESSABLE_ENTITY, message, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionApi> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ErrorTypes.UNPROCESSABLE_ENTITY, ex.getMessage(), request);
    }

    @ExceptionHandler(HandleGeneric.class)
    public ResponseEntity<ExceptionApi> handleGeneric(HandleGeneric ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorTypes.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionApi> handleUnexpected(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ErrorTypes.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
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

    private String resolveConstraintMessage(DataIntegrityViolationException ex) {
        String root = getRootMessage(ex);

        if (root.contains("uq_queue_code_per_desk")) {
            return "QUEUE_CODE_ALREADY_EXISTS_FOR_DESK";
        }

        if (root.contains("uq_ticket_public_code_per_desk")) {
            return "TICKET_PUBLIC_CODE_ALREADY_EXISTS_FOR_DESK";
        }
        if (root.contains("uq_session_per_ticket")) {
            return "SESSION_ALREADY_EXISTS_FOR_TICKET";
        }

        return "RESOURCE_CONFLICT";
    }

    private String getRootMessage(Throwable t) {
        Throwable cur = t;
        while (cur.getCause() != null) cur = cur.getCause();
        return Objects.toString(cur.getMessage(), "");
    }
}