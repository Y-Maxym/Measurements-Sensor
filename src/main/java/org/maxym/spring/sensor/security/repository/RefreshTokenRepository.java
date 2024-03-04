package org.maxym.spring.sensor.security.repository;

import org.maxym.spring.sensor.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    void deleteByUser_Username(String username);

    @Query("SELECT r.user.username FROM RefreshToken r")
    String getUsernameByToken(String token);
}
