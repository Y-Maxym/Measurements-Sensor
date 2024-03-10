package org.maxym.spring.sensor.exception;

import lombok.Getter;
import org.maxym.spring.sensor.error.FieldErrorEntity;

import java.util.List;

@Getter
public abstract class ApplicationException extends RuntimeException {

    private final List<FieldErrorEntity> errors;

    public ApplicationException(String message, List<FieldErrorEntity> errors) {
        super(message);
        this.errors = errors;
    }
}
