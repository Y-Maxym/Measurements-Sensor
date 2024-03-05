package org.maxym.spring.sensor.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "role",
        schema = "public",
        uniqueConstraints = @UniqueConstraint(name = "unique_role_name", columnNames = "name"),
        indexes = @Index(name = "idx_role_name", columnList = "name", unique = true))
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}
