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
import org.maxym.spring.sensor.service.*;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.maxym.spring.sensor.util.validator.UserRequestValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final JWTService JWTService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;
    private final BindingResultService bindingResultService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Validated UserRequest userRequest,
                                    BindingResult bindingResult,
                                    HttpServletResponse response) {

        userRequestValidator.validate(userRequest, bindingResult);
        bindingResultService.handle(bindingResult, UserCreationException::new);

        User user = userMapper.map(userRequest);
        userService.save(user);

        String accessToken = JWTService.generateToken(userRequest.username());
        String refreshToken = refreshTokenService.generateToken(userRequest.username()).getToken();

        refreshTokenService.addRTokenToCookies(refreshToken, response);
        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest,
                                   HttpServletResponse response) {

        String username = loginRequest.username();
        String password = loginRequest.password();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        refreshTokenService.deleteByUser_Username(username);
        String accessToken = JWTService.generateToken(username);
        String refreshToken = refreshTokenService.generateToken(username).getToken();

        refreshTokenService.addRTokenToCookies(refreshToken, response);
        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.status(HttpStatus.OK).body("User authenticated successfully");

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        tokenService.deleteCookie("refreshToken", response);
        tokenService.deleteHeader("Authorization", response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request,
                                                HttpServletResponse response) {

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null || !refreshTokenService.validateToken(refreshToken)) {
            throw new RefreshTokenException("Invalid Refresh Token");
        }

        String username = refreshTokenService.findUsernameByToken(refreshToken);
        if (username == null) {
            throw new RefreshTokenException("Refresh Token is not linked to any user");
        }

        refreshTokenService.deleteByToken(refreshToken);
        String newRefreshToken = refreshTokenService.generateToken(username).getToken();
        String accessToken = JWTService.generateToken(username);

        refreshTokenService.addRTokenToCookies(newRefreshToken, response);
        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Refresh successful");
    }
}