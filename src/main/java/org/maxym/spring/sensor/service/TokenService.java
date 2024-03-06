package org.maxym.spring.sensor.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    public void deleteCookie(String cookieName, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public void deleteHeader(String header, HttpServletResponse response) {
        response.setHeader(header, "");
    }
}
