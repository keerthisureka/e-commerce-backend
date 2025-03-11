package com.example.auth_server.Controllers;

import com.example.auth_server.dto.JWTRequest;
import com.example.auth_server.dto.JWTResponse;
import com.example.auth_server.dto.UserDTO;
import com.example.auth_server.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {
        return authService.doAuthenticate(request);
    }


    @PostMapping("/create-user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        return authService.createUser(userDTO);
    }
}
