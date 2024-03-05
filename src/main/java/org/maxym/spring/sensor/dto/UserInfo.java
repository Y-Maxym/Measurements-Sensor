package org.maxym.spring.sensor.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserInfo {
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
