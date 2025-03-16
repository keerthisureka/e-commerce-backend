package com.example.auth_server.Controllers;

import com.example.auth_server.dto.JWTRequest;
import com.example.auth_server.dto.JWTResponse;
import com.example.auth_server.dto.UserDTO;
import com.example.auth_server.service.AuthService;
import com.example.auth_server.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:8090", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JWTUtils jwtUtils;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JWTRequest request, HttpServletResponse response) {
        ResponseEntity<JWTResponse> authResponse = authService.doAuthenticate(request);
        JWTResponse jwtResponse = authResponse.getBody();

        if (jwtResponse != null) {
            Cookie cookie = new Cookie("jwt", jwtResponse.getJwtToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) JWTUtils.JWT_TOKEN_VALIDITY); // Expiration in seconds
            response.addCookie(cookie);
            response.addHeader("Set-Cookie",
                    "jwt=" + jwtResponse.getJwtToken() + "; Path=/; HttpOnly; SameSite=Lax");
        }
        return authResponse;
    }

    @PostMapping("/create-user")
    public ResponseEntity<JWTResponse> createUser(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        ResponseEntity<JWTResponse> authResponse = authService.createUser(userDTO);
        JWTResponse jwtResponse = authResponse.getBody();

        if (jwtResponse != null) {
            Cookie cookie = new Cookie("jwt", jwtResponse.getJwtToken());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) JWTUtils.JWT_TOKEN_VALIDITY); // Expiration in seconds
            response.addCookie(cookie);
        }
        return authResponse;
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        boolean isValid = jwtUtils.validateToken(token);
        System.out.println(isValid);
        return ResponseEntity.ok(isValid);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    String username = jwtUtils.getUsernameFromToken(token);

                    // Remove the token from Redis
                    jwtUtils.removeToken(username);

                    // Clear the cookie by setting its max age to 0
                    cookie.setMaxAge(0);
                    cookie.setValue(null);
                    cookie.setPath("/"); // Make sure the path matches your cookie settings
                    response.addCookie(cookie);
                    return ResponseEntity.ok("Logged out successfully");
                }
            }
        }
        return ResponseEntity.badRequest().body("No JWT cookie found");
    }
}
