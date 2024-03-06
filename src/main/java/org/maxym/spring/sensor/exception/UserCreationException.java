package org.maxym.spring.sensor.exception;

import org.maxym.spring.sensor.error.FieldErrorResponse;

import java.util.List;

public class UserCreationException extends ApplicationException {
    public UserCreationException(String message, List<FieldErrorResponse> errors) {
        super(message, errors);
    }
}
