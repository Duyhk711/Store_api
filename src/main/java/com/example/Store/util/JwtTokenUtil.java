package com.example.Store.util;

import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.Store.entity.User;

@Component
public class JwtTokenUtil {
    @Lazy

    private String SECRET_KEY = "DUY_07";

    public String generateToken(User user, Long expiredDate) {
        String token = JWT.create()
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expiredDate * 1000))
                .sign(Algorithm.HMAC512(SECRET_KEY));

        System.out.println("Generated Token: " + token); 
        return token;
    }

    public DecodedJWT decodeToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(SECRET_KEY))
                    .build()
                    .verify(token);
        } catch (JWTVerificationException e) {
            System.out.println("Token verification failed: " + e.getMessage());
            return null; 
        }
    }
    
    public String extractEmail(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        if (decodedJWT != null) {
            String email = decodedJWT.getSubject();
            // System.out.println("Decoded Email: " + email); 
            return email; 
        } else {
            System.out.println("Decoded JWT is null."); 
            return null;
        }
    }


    public boolean validateToken(String token) {
        String email = extractEmail(token);
        return (email != null && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return decodeToken(token).getExpiresAt().before(new Date());
    }
}
