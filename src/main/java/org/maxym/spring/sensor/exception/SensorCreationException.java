package org.maxym.spring.sensor.exception;

import lombok.Getter;
import org.maxym.spring.sensor.error.FieldErrorResponse;

import java.util.List;

@Getter
public class SensorCreationException extends ApplicationException {

    public SensorCreationException(List<FieldErrorResponse> errors) {
        super("Sensor creation error", errors);
    }
}
