package org.maxym.spring.sensor.util.responce.exception;

import lombok.Getter;
import org.maxym.spring.sensor.util.responce.error.SensorFieldErrorResponse;

import java.util.List;

@Getter
public class SensorCreationException extends SensorException {

    public SensorCreationException(String message, List<SensorFieldErrorResponse> errors) {
        super(message, errors);
    }
}
