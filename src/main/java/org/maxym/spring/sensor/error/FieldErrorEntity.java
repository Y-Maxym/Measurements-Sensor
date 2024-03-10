package org.maxym.spring.sensor.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.FieldError;

@Data
@AllArgsConstructor
public class FieldErrorEntity {
    private String field;
    private String message;

    public FieldErrorEntity(FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
    }
}
