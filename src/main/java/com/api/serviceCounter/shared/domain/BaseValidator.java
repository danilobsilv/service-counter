package com.api.serviceCounter.shared.domain;

public interface BaseValidator<T>{
    void validate(T data);
}
