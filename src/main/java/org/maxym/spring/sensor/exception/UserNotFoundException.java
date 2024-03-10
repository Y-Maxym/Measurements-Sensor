package org.maxym.spring.sensor.exception;

public class UserNotFoundException extends SimpleApplicationException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
