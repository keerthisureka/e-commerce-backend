package com.example.ApiGateway.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class JWTUtils {
    @Value("${jwt.secret}")
    private String secret;

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extracts the subject (which is email) from the token.
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // New method: Extract the custom "userId" claim from the token.
    public String getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("userId", String.class));
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        System.out.println("token: " + token + " claim: "  + claims);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
