package org.maxym.spring.sensor.util.responce.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SensorErrorResponse {
    private String message;
    private List<SensorFieldErrorResponse> errors;
    private long timestamp;
}