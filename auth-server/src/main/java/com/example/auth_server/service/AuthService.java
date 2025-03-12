package com.example.auth_server.service;

import com.example.auth_server.dto.JWTRequest;
import com.example.auth_server.dto.JWTResponse;
import com.example.auth_server.dto.UserDTO;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    public ResponseEntity<JWTResponse> doAuthenticate(JWTRequest request);
    public ResponseEntity<JWTResponse> createUser(UserDTO userDTO);
}
