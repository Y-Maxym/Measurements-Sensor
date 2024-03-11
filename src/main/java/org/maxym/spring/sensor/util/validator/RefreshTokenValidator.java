package org.maxym.spring.sensor.util.validator;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.RefreshTokenException;
import org.maxym.spring.sensor.model.RefreshToken;
import org.maxym.spring.sensor.service.RefreshTokenService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
        RefreshToken refreshToken = refreshTokenService.findByToken(token);
        return refreshToken.getExpiryDate().isAfter(LocalDateTime.now());
    }
}
