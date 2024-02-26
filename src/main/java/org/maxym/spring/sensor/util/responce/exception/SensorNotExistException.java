package org.maxym.spring.sensor.util.responce.exception;

import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;

import java.util.List;

public class SensorNotExistException extends ApplicationException {
    public SensorNotExistException(String message, List<FieldErrorResponse> errors) {
        super(message, errors);
    }
}