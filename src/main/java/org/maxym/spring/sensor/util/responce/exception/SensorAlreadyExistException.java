package org.maxym.spring.sensor.util.responce.exception;

import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;

import java.util.List;

public class SensorAlreadyExistException extends ApplicationException {
    public SensorAlreadyExistException(String message, List<FieldErrorResponse> errors) {
        super(message, errors);
    }
}
