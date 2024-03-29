package org.maxym.spring.sensor.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.AccessTokenException;
import org.maxym.spring.sensor.security.service.JWTService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JWTService JWTService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader("Authorization");
        try {
            if (nonNull(authorization) && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);
                String username = JWTService.extractUsername(token);

                if (nonNull(username) && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );

                    if (JWTService.isValid(token, username))
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (SignatureException exception) {
            request.setAttribute("exception", new AccessTokenException("Invalid access token"));
            request.getRequestDispatcher("/error/catch").forward(request, response);
        } catch (ExpiredJwtException exception) {
            request.setAttribute("exception", new AccessTokenException("Access token has expired"));
            request.getRequestDispatcher("/error/catch").forward(request, response);
        }
    }
}
