package org.maxym.spring.sensor.exception;

import lombok.Getter;
import org.maxym.spring.sensor.error.FieldErrorEntity;

import java.util.List;

@Getter
public class SensorCreationException extends ApplicationException {

    public SensorCreationException(List<FieldErrorEntity> errors) {
        super("Sensor creation error", errors);
    }
}
