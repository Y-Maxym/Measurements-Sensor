package org.maxym.spring.sensor.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "role",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(name = "unique_role_role", columnNames = "role"),
        indexes = @Index(name = "idx_role_role", columnList = "role", unique = true))
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "role", nullable = false, unique = true, length = 100)
    private String role;
}
