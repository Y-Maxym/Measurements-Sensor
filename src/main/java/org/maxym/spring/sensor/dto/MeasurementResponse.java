package org.maxym.spring.sensor.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MeasurementResponse {
    private Double value;
    private Boolean raining;
    private String sensor;
}
