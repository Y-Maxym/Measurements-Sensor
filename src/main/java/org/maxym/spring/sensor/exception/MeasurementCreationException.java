package org.maxym.spring.sensor.exception;

import org.maxym.spring.sensor.error.FieldErrorResponse;

import java.util.List;

public class MeasurementCreationException extends ApplicationException {
    public MeasurementCreationException(List<FieldErrorResponse> errors) {
        super("Measurement creation error", errors);
    }
}
