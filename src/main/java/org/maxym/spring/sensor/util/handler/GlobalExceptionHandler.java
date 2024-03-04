package org.maxym.spring.sensor.util.handler;

import org.maxym.spring.sensor.util.responce.error.ErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ErrorResponse> handleException(UserCreationException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleException(AuthenticationException ignore) {
        return new ResponseEntity<>("Bad credentials", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SensorNotExistException.class)
    private ResponseEntity<ErrorResponse> handleException(SensorNotExistException exception) {
        ErrorResponse response = new ErrorResponse("An error occurred.", exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MeasurementCreationException.class)
    private ResponseEntity<ErrorResponse> handleException(MeasurementCreationException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SensorCreationException.class)
    private ResponseEntity<ErrorResponse> handleException(SensorCreationException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SensorAlreadyExistException.class)
    private ResponseEntity<ErrorResponse> handleException(SensorAlreadyExistException exception) {
        ErrorResponse response = new ErrorResponse("An error occurred.", exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}