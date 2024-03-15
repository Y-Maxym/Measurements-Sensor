package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.LoginRequestDto;
import org.maxym.spring.sensor.dto.UserRequestDto;
import org.maxym.spring.sensor.exception.UserCreationException;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.security.service.AuthService;
import org.maxym.spring.sensor.service.BindingResultService;
import org.maxym.spring.sensor.service.RefreshTokenService;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.maxym.spring.sensor.util.validator.RefreshTokenValidator;
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
    private final RefreshTokenValidator refreshTokenValidator;
    private final AuthService authService;
    private final BindingResultService bindingResultService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Validated UserRequestDto userRequestDto,
                                    BindingResult bindingResult) {

        String username = userRequestDto.username();

        userRequestValidator.validate(userRequestDto, bindingResult);
        bindingResultService.handle(bindingResult, UserCreationException::new);

        User user = userMapper.map(userRequestDto);
        userService.save(user);

        HttpHeaders headers = new HttpHeaders();
        authService.accessToken(username, headers);
        authService.refreshToken(username, headers);

        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {

        String username = loginRequestDto.username();
        String password = loginRequestDto.password();

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
                                                String token) {

        refreshTokenValidator.validate(token);

        String username = refreshTokenService.findUsernameByToken(token);

        HttpHeaders headers = new HttpHeaders();
        authService.accessToken(username, headers);
        authService.refreshToken(username, headers);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body("Refresh successful");
    }
}