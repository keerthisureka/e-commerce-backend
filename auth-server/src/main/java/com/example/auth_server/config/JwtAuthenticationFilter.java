package com.example.auth_server.config;

import com.example.auth_server.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JWTUtils jwtUtilHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate; // Inject Redis

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // First, try to extract the token from a cookie
        String token = getJwtFromCookie(request);

        // If token is not found in cookies, check the Authorization header
        if (token == null) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }
        }

        // Proceed only if token is found
        if (token != null) {
            String username = null;
            try {
                username = jwtUtilHelper.getUsernameFromToken(token);
            } catch (Exception e) {
                logger.error("Error extracting username from token", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT Token");
                return;
            }

            String redisKey = "JWT:" + username;
            String storedToken = redisTemplate.opsForValue().get(redisKey);

            if (storedToken == null || !storedToken.equals(token)) {
                logger.info("JWT Token not found in Redis or does not match.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT Token");
                return;
            }

            try {
                if (!jwtUtilHelper.validateToken(token)) {
                    logger.info("Invalid JWT Token.");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid JWT Token");
                    return;
                }
            } catch (Exception e) {
                logger.error("Token validation error", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Helper method to extract the JWT from cookies
    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}