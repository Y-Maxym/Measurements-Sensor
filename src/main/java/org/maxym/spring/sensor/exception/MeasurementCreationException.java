package org.maxym.spring.sensor.exception;

import org.maxym.spring.sensor.error.FieldErrorEntity;

import java.util.List;

public class MeasurementCreationException extends ApplicationException {
    public MeasurementCreationException(List<FieldErrorEntity> errors) {
        super("Measurement creation error", errors);
    }
}
