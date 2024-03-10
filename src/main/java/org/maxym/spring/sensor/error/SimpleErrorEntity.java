package org.maxym.spring.sensor.error;

public record SimpleErrorEntity(
        String message,
        long timestamp
) {
}
