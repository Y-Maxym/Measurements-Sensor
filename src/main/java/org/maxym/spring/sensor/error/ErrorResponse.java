package org.maxym.spring.sensor.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private List<FieldErrorResponse> errors;
    private long timestamp;
}