package com.cybercrimeanalytics.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    private final SecretKey key;
    private final long accessMinutes;
    private final long refreshDays;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.access-token-minutes}") long accessMinutes,
                      @Value("${app.jwt.refresh-token-days}") long refreshDays) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessMinutes = accessMinutes;
        this.refreshDays = refreshDays;
    }

    public String accessToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), Map.of("type", "access"), Instant.now().plusSeconds(accessMinutes * 60));
    }

    public String refreshToken(UserDetails userDetails) {
        return buildToken(userDetails.getUsername(), Map.of("type", "refresh"), Instant.now().plusSeconds(refreshDays * 86400));
    }

    public String subject(String token) {
        return claims(token).getSubject();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        return subject(token).equals(userDetails.getUsername()) && claims(token).getExpiration().after(new Date());
    }

    public boolean isRefreshToken(String token) {
        return "refresh".equals(claims(token).get("type", String.class));
    }

    private String buildToken(String subject, Map<String, Object> claims, Instant expiresAt) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date())
                .expiration(Date.from(expiresAt))
                .signWith(key)
                .compact();
    }

    private Claims claims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }
}
