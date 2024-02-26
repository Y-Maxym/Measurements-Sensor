package org.maxym.spring.sensor.util.responce.exception;

import org.maxym.spring.sensor.util.responce.error.SensorFieldErrorResponse;

import java.util.List;

public class SensorAlreadyExistException extends SensorException {
    public SensorAlreadyExistException(String message, List<SensorFieldErrorResponse> errors) {
        super(message, errors);
    }
}
