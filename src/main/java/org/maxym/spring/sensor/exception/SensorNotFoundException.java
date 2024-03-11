package org.maxym.spring.sensor.exception;

public class SensorNotFoundException extends SimpleApplicationException {
    public SensorNotFoundException(String message) {
        super(message);
    }
}
