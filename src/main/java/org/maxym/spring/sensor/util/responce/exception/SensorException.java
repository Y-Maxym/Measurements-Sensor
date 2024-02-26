package org.maxym.spring.sensor.util.responce.exception;

import lombok.Getter;
import org.maxym.spring.sensor.util.responce.error.SensorFieldErrorResponse;

import java.util.List;

@Getter
public abstract class SensorException extends RuntimeException {

    private final List<SensorFieldErrorResponse> errors;

    public SensorException(String message, List<SensorFieldErrorResponse> errors) {
        super(message);
        this.errors = errors;
    }
}
