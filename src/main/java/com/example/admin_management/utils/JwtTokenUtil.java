package com.example.admin_management.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final int TOKEN_EXPIRED_TIME = 24 * 60 * 60 * 1000;
    private static final Key TOKEN_SECRET_KEY = Keys.hmacShaKeyFor("93qw665rZXF5VUWyeheDQkXwUL38e4LlZdu6cHdu72scYwC4v0khCl+PgoKOGcC81ttjhli49O5kG5/T1LbCdQ==".getBytes());

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRED_TIME))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET_KEY)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(TOKEN_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Instant expiryDate = claims.getExpiration().toInstant();
        Instant checkTime = Instant.now().minus(30, ChronoUnit.MINUTES);
        return expiryDate.isBefore(checkTime);
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(getUsernameFromToken(token)) && !isTokenExpired(token));
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

}
