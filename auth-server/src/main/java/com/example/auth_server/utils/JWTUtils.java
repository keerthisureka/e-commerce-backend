package com.example.auth_server.utils;

import com.example.auth_server.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtils {

    public static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60; // in seconds

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String generateToken(UserDetails userDetails) {
        String userId = ((User) userDetails).getUserId();
        String token = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        // Store token in Redis with expiry
        String redisKey = "JWT:" + userDetails.getUsername();
        redisTemplate.opsForValue().set(redisKey, token, Duration.ofSeconds(JWT_TOKEN_VALIDITY));

        return token;
    }

    public String getOrGenerateToken(UserDetails userDetails) {
        String redisKey = "JWT:" + userDetails.getUsername();
        String token = redisTemplate.opsForValue().get(redisKey);
        // Optionally, you might want to check if the token is valid and not expired
        if (token == null || !validateToken(token)) {
            token = generateToken(userDetails);
        }
        System.out.println(getUserIdFromToken(token));
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void removeToken(String username) {
        String redisKey = "JWT:" + username;
        redisTemplate.delete(redisKey);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public String getUserIdFromToken(String token) {
        String userId = getClaimFromToken(token, claims -> claims.get("userId", String.class));
        if (userId == null) {
            // Fallback: use the subject
            userId = getClaimFromToken(token, Claims::getSubject);
        }
        return userId;
    }
}
