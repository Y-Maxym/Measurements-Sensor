package org.maxym.spring.sensor.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public void authenticate(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
    }

    public void accessToken(String username, HttpServletResponse response) {
        String token = jwtService.generateToken(username);
        response.setHeader("Authorization", "Bearer " + token);
    }

    public void refreshToken(String username, HttpServletResponse response) {
        refreshTokenService.deleteByUser_Username(username);
        String token = refreshTokenService.generateToken(username);
        addRTokenToCookies(token, response);
    }

    public void addRTokenToCookies(String refreshToken, HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(-1);

        response.addCookie(refreshTokenCookie);
    }

    public void deleteCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void deleteHeader(String header, HttpServletResponse response) {
        response.setHeader(header, "");
    }
}
