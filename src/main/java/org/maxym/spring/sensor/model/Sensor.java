package org.maxym.spring.sensor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(name = "unique_sensor_name", columnNames = "name"),
        indexes = @Index(name = "idx_sensor_name", columnList = "name", unique = true))
@Data
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by", referencedColumnName = "id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_sensor_created_by",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (created_by) REFERENCES \"user\"(id) ON DELETE CASCADE"))
    private User createdBy;
}

