package org.maxym.spring.sensor.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.LoginRequest;
import org.maxym.spring.sensor.dto.UserRequest;
import org.maxym.spring.sensor.exception.RefreshTokenException;
import org.maxym.spring.sensor.exception.UserCreationException;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.service.AuthService;
import org.maxym.spring.sensor.service.BindingResultService;
import org.maxym.spring.sensor.service.RefreshTokenService;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.maxym.spring.sensor.util.validator.UserRequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRequestValidator userRequestValidator;
    private final RefreshTokenService refreshTokenService;
    private final AuthService authService;
    private final BindingResultService bindingResultService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Validated UserRequest userRequest,
                                    BindingResult bindingResult,
                                    HttpServletResponse response) {

        String username = userRequest.username();

        userRequestValidator.validate(userRequest, bindingResult);
        bindingResultService.handle(bindingResult, UserCreationException::new);

        User user = userMapper.map(userRequest);
        userService.save(user);

        authService.accessToken(username, response);
        authService.refreshToken(username, response);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletResponse response) {

        String username = loginRequest.username();
        String password = loginRequest.password();

        authService.authenticate(username, password);

        authService.accessToken(username, response);
        authService.refreshToken(username, response);

        return ResponseEntity.status(HttpStatus.OK).body("User authenticated successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        authService.deleteCookie("refreshToken", response);
        authService.deleteHeader("Authorization", response);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request,
                                                HttpServletResponse response) {

        // TODO: auth service
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        // TODO: auth service
        if (refreshToken == null || !refreshTokenService.isValid(refreshToken)) {
            throw new RefreshTokenException("Invalid Refresh Token");
        }

        // TODO: auth service
        String username = refreshTokenService.findUsernameByToken(refreshToken);
        if (username == null) {
            throw new RefreshTokenException("Refresh Token is not linked to any user");
        }

        authService.accessToken(username, response);
        authService.refreshToken(username, response);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Refresh successful");
    }
}