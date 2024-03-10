package org.maxym.spring.sensor.exception;

import org.maxym.spring.sensor.error.FieldErrorEntity;

import java.util.List;

public class UserCreationException extends ApplicationException {
    public UserCreationException(List<FieldErrorEntity> errors) {
        super("User creation error", errors);
    }
}
