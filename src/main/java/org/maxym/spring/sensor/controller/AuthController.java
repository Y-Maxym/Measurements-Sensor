package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.LoginRequest;
import org.maxym.spring.sensor.dto.UserRequest;
import org.maxym.spring.sensor.exception.RefreshTokenException;
import org.maxym.spring.sensor.exception.UserCreationException;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.security.service.AuthService;
import org.maxym.spring.sensor.service.BindingResultService;
import org.maxym.spring.sensor.service.RefreshTokenService;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.maxym.spring.sensor.util.validator.UserRequestValidator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
                                    BindingResult bindingResult) {

        String username = userRequest.username();

        userRequestValidator.validate(userRequest, bindingResult);
        bindingResultService.handle(bindingResult, UserCreationException::new);

        User user = userMapper.map(userRequest);
        userService.save(user);

        HttpHeaders headers = new HttpHeaders();
        authService.accessToken(username, headers);
        authService.refreshToken(username, headers);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        String username = loginRequest.username();
        String password = loginRequest.password();

        authService.authenticate(username, password);

        HttpHeaders headers = new HttpHeaders();
        authService.accessToken(username, headers);
        authService.refreshToken(username, headers);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body("User authenticated successfully");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {

        HttpHeaders headers = new HttpHeaders();
        authService.deleteCookie("refreshToken", headers);
        authService.deleteHeader("Authorization", headers);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(name = "refreshToken", required = false)
                                                String refreshToken) {

        // TODO: auth service
        if (refreshToken == null || !refreshTokenService.isValid(refreshToken)) {
            throw new RefreshTokenException("Invalid Refresh Token");
        }

        // TODO: auth service
        String username = refreshTokenService.findUsernameByToken(refreshToken);
        if (username == null) {
            throw new RefreshTokenException("Refresh Token is not linked to any user");
        }

        HttpHeaders headers = new HttpHeaders();
        authService.accessToken(username, headers);
        authService.refreshToken(username, headers);

        return ResponseEntity.status(HttpStatus.ACCEPTED).headers(headers).body("Refresh successful");
    }
}