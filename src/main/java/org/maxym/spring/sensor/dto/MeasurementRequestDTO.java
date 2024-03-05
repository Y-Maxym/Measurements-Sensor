package org.maxym.spring.sensor.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MeasurementRequestDTO(

        @NotNull(message = "Value should not be empty")
        @Max(value = 100, message = "Value should be less or equals 100")
        @Min(value = -100, message = "Value should be greater of equals -100")
        Double value,

        @NotNull(message = "Raining should not be empty")
        Boolean raining,

        @NotNull(message = "Sensor should not be empty")
        SensorRequestDTO sensor
) {
}
