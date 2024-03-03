package org.maxym.spring.sensor.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.security.service.AuthDetailsService;
import org.maxym.spring.sensor.security.service.JWTService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final AuthDetailsService authDetailsService;
    private final JWTService JWTService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader("Authorization");

        if (nonNull(authorization) && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            String username = JWTService.extractUsername(token);

            if (nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = authDetailsService.loadUserByUsername(username);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

                if (JWTService.validateToken(token, username))
                    SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
