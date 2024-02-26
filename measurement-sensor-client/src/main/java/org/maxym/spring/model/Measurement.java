package org.maxym.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    private Double value;
    private Boolean raining;
    private Sensor sensor;
}
