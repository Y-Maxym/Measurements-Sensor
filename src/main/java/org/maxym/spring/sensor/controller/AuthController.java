package org.maxym.spring.sensor.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.LoginRequest;
import org.maxym.spring.sensor.dto.UserRequestDTO;
import org.maxym.spring.sensor.model.RefreshToken;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.security.service.JWTService;
import org.maxym.spring.sensor.security.service.RefreshTokenService;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.maxym.spring.sensor.util.request.validator.UserRequestValidator;
import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.UserCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JWTService JWTService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserRequestValidator userRequestValidator;

    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@RequestBody @Validated UserRequestDTO userRequestDTO,
                                           BindingResult bindingResult,
                                           HttpServletResponse response) {

        userRequestValidator.validate(userRequestDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldErrorResponse> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorResponse::new)
                    .forEach(errors::add);

            throw new UserCreationException("An error occurred.", errors);
        }

        User user = userMapper.map(userRequestDTO);
        userService.save(user);

        String accessToken = JWTService.generateToken(userRequestDTO.getUsername());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userRequestDTO.getUsername());

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken.getToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(refreshTokenCookie);
        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest,
                                        HttpServletResponse response) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        refreshTokenService.deleteByUser_Username(username);

        String accessToken = JWTService.generateToken(username);
        String refreshToken = refreshTokenService.createRefreshToken(username).getToken();

        refreshTokenService.addRefreshTokenToResponse(refreshToken, response);

        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.status(HttpStatus.OK).body("User authenticated successfully");

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request,
                                                HttpServletResponse response) {

        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken == null || !refreshTokenService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Refresh Token");
        }

        String username = refreshTokenService.findUsernameByToken(refreshToken);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh Token is not linked to any user");
        }

        refreshTokenService.deleteByToken(refreshToken);

        String newRefreshToken = refreshTokenService.createRefreshToken(username).getToken();
        String accessToken = JWTService.generateToken(username);

        refreshTokenService.addRefreshTokenToResponse(newRefreshToken, response);

        response.setHeader("Authorization", "Bearer " + accessToken);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Refresh successful");
    }
}