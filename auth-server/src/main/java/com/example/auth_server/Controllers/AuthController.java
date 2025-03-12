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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTUtils jwtUtils;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {
        return authService.doAuthenticate(request);
    }


    @PostMapping("/create-user")
    public ResponseEntity<JWTResponse> createUser(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        return authService.createUser(userDTO);
    }

    @GetMapping("/validateToken")
    public ResponseEntity<Boolean> validateToken(@RequestParam("token") String token) {
        boolean isValid = jwtUtils.validateToken(token);
        System.out.println(isValid);
        return ResponseEntity.ok(isValid);
    }
}
