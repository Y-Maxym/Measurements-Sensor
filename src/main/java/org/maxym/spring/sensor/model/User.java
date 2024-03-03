package org.maxym.spring.sensor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.maxym.spring.sensor.model.enums.Authorities;

import java.util.Set;

@Entity
@Table(name = "\"user\"")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @ElementCollection(targetClass = Authorities.class, fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @Column(name = "authorities")
    private Set<Authorities> authorities;

}
