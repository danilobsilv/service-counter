package com.api.serviceCounter.shared.errors.validationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class HandleValidation extends RuntimeException{
    public HandleValidation(String message){
        super(message);
    }
}
