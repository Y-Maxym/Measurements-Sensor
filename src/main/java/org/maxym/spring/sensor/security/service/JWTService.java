package org.maxym.spring.sensor.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JWTService {

    private final SecretKey key = Jwts.SIG.HS256.key().build();

    public String generateToken(String username) {
        return Jwts.builder()
                .subject("Measurement-Sensor")
                .claim("username", username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .issuer("dec")
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.get("username", String.class));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        return !isTokenExpired(token) && extractUsername(token).equals(username);
    }
}
