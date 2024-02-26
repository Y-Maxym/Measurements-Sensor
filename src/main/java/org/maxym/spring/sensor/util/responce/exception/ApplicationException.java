package org.maxym.spring.sensor.util.responce.exception;

import lombok.Getter;
import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;

import java.util.List;

@Getter
public abstract class ApplicationException extends RuntimeException {

    private final List<FieldErrorResponse> errors;

    public ApplicationException(String message, List<FieldErrorResponse> errors) {
        super(message);
        this.errors = errors;
    }
}
