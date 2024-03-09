package org.maxym.spring.sensor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurement", schema = "public")
@Data
public class Measurement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Integer id;

    @Column(name = "value", nullable = false, updatable = false, precision = 4, scale = 1)
    private BigDecimal value;

    @Column(name = "raining", nullable = false, updatable = false)
    private Boolean raining;

    @ManyToOne(cascade = CascadeType.REMOVE, optional = false)
    @JoinColumn(name = "sensor_id", referencedColumnName = "id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_measurement_sensor_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (sensor_id) REFERENCES sensor(id) ON DELETE CASCADE"))
    private Sensor sensor;

    @CreationTimestamp
    @Column(name = "measurement_date", nullable = false, updatable = false)
    private LocalDateTime measurementDate;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
