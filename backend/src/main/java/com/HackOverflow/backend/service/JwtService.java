package com.HackOverflow.backend.service;

import com.HackOverflow.backend.model.Users;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "pH346RCbVVaW0MEtK0xbsskEou+/yrUZEb2TL5zr1pY=";

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
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(SECRET)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}