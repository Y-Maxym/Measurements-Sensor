package org.maxym.spring.sensor.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.maxym.spring.sensor.model.Sensor;

@Data
public class MeasurementDTO {

    @NotNull(message = "Value should not be empty")
    @Max(value = 100, message = "Value should be less or equals 100")
    @Min(value = -100, message = "Value should be greater of equals -100")
    private Double value;

    @NotNull(message = "Raining should not be empty")
    private Boolean raining;

    @NotNull(message = "Sensor should not be empty")
    private Sensor sensor;
}
