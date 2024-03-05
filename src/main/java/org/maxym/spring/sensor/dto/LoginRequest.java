package org.maxym.spring.sensor.dto;

public record LoginRequest(
        String username,
        String password
) {
}
