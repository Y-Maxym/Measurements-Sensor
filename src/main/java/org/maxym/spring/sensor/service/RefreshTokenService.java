package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.RefreshTokenException;
import org.maxym.spring.sensor.model.RefreshToken;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Value("${jwt.refresh.duration}")
    private Duration duration;

    @Transactional
    public String generateToken(String username) {
        User user = userService.findByUsername(username);

        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plus(duration.toMillis(), ChronoUnit.MILLIS))
                .build();

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Token not found."));
    }

    @Transactional
    public void deleteByUser_Username(String username) {
        refreshTokenRepository.deleteByUser_Username(username);
    }

    public String findUsernameByToken(String token) {
        return refreshTokenRepository.findUsernameByToken(token);
    }
}
