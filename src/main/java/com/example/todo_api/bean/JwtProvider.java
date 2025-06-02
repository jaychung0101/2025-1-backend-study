package com.example.todo_api.bean;

import com.example.todo_api.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String createToken(String email) {
        SecretKey key = new SecretKeySpec(
                jwtProperties.getSecret().getBytes(),
                SignatureAlgorithm.HS256.getJcaName()
        );

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        SecretKey key = new SecretKeySpec(
                jwtProperties.getSecret().getBytes(),
                SignatureAlgorithm.HS256.getJcaName()
        );

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}