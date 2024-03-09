package org.maxym.spring.sensor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "\"user\"", schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(name = "unique_user_username", columnNames = "username"),
                @UniqueConstraint(name = "unique_user_email", columnNames = "email")
        },
        indexes = @Index(name = "idx_user_username", columnList = "username", unique = true))
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            schema = "public",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName = "id",
                    nullable = false),
            foreignKey = @ForeignKey(name = "fk_user_role_user_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES \"user\"(id) ON DELETE CASCADE"),
            inverseForeignKey = @ForeignKey(name = "fk_user_role_role_id",
                    value = ConstraintMode.CONSTRAINT,
                    foreignKeyDefinition = "FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE"))
    private Set<Role> roles;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
