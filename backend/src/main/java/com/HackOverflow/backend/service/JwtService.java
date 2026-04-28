package com.HackOverflow.backend.service;

import com.HackOverflow.backend.model.Users;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);


    @Value("${jwt.secret}")
    private String SECRET;

    public String generateToken(Users user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRoleType().name())
                .claim("profileCompleted", user.isProfileCompleted())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET)))
                .compact();
    }

    public String extractEmail(String token) {
        log.info("token = "+token);
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}