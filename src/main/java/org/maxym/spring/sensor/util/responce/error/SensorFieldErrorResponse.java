package org.maxym.spring.sensor.util.responce.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
@AllArgsConstructor
public class SensorFieldErrorResponse {
    private String field;
    private String message;

    public SensorFieldErrorResponse(FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
    }
}
