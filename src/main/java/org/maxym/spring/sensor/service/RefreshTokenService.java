package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.model.RefreshToken;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.repository.RefreshTokenRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Transactional
    public String generateToken(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));

        String token = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        return refreshTokenRepository.save(refreshToken).getToken();
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public void deleteByUser_Username(String username) {
        refreshTokenRepository.deleteByUser_Username(username);
    }

    public String findUsernameByToken(String token) {
        return refreshTokenRepository.findUsernameByToken(token);
    }
}
