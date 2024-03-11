package org.maxym.spring.sensor.util.validator;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.RefreshTokenException;
import org.maxym.spring.sensor.model.RefreshToken;
import org.maxym.spring.sensor.service.RefreshTokenService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class RefreshTokenValidator {

    private final RefreshTokenService refreshTokenService;

    public void validate(String token) {
        if (isNull(token) || !isValid(token)) {
            throw new RefreshTokenException("Invalid Refresh Token");
        }
    }

    private boolean isValid(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.findByToken(token);
        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();
            return refreshToken.getExpiryDate().isAfter(LocalDateTime.now());
        }
        return false;
    }
}
