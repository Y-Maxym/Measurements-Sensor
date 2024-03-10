package org.maxym.spring.sensor.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorEntity {
    private String message;
    private List<FieldErrorEntity> errors;
    private long timestamp;
}