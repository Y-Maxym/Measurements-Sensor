package org.maxym.spring.sensor.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public record SensorRequestDTO(

        @NotEmpty(message = "Sensor name should not be empty")
        @Size(min = 3, max = 30, message = "Sensor name should be between 3 and 30 characters")
        String name
) {
}
