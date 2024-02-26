package org.maxym.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Measurement {
    private Double value;
    private Boolean raining;
    private Sensor sensor;
}
