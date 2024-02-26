package org.maxym.spring.sensor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "measurement", schema = "public")
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull(message = "Value should not be empty")
    @Max(value = 100, message = "Value should be less or equals 100")
    @Min(value = -100, message = "Value should be greater of equals -100")
    @Column(name = "value")
    private Double value;

    @NotNull(message = "Raining should not be empty")
    @Column(name = "raining")
    private Boolean raining;

    @NotNull(message = "Sensor should not be empty")
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private Sensor sensor;

    @CreationTimestamp
    @Column(name = "measurement_date")
    private LocalDateTime measurementDate;
}
