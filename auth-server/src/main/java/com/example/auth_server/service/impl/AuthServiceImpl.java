package com.example.auth_server.service.impl;

import com.example.auth_server.dto.JWTRequest;
import com.example.auth_server.dto.JWTResponse;
import com.example.auth_server.dto.UserDTO;
import com.example.auth_server.entity.User;
import com.example.auth_server.repositories.UserRepository;
import com.example.auth_server.service.AuthService;
import com.example.auth_server.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

//    @Override
//    public ResponseEntity<JWTResponse> doAuthenticate(JWTRequest request) {
//        String email = request.getEmail();
//        String password = request.getPassword();
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
//        try {
//            manager.authenticate(authenticationToken);
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//        // Generate token only if not present in Redis (or if the present token is invalid)
//        String token = jwtUtils.getOrGenerateToken(userDetails);
//        JWTResponse response = new JWTResponse();
//        response.setJwtToken(token);
//        return ResponseEntity.ok(response);
//    }

//    @Override
//    public ResponseEntity<JWTResponse> createUser(UserDTO userDTO) {
//        // Check if a user with the provided email already exists
//        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
//        if (existingUser.isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
//        }
//        // Create new user if not existing
//        User user = userService.createUser(userDTO);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
//        String token = jwtUtils.getOrGenerateToken(userDetails);
//        JWTResponse response = new JWTResponse();
//        response.setJwtToken(token);
//        return ResponseEntity.ok(response);
//    }

    @Override
    public ResponseEntity<JWTResponse> doAuthenticate(JWTRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtils.getOrGenerateToken(userDetails);
        JWTResponse response = new JWTResponse();
        response.setJwtToken(token);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<JWTResponse> createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        // Create and save user
        User user = userService.createUser(userDTO);
        userRepository.save(user);  // Ensure user is saved before fetching
        // Fetch from database again
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtils.getOrGenerateToken(userDetails);
        JWTResponse response = new JWTResponse();
        response.setJwtToken(token);
        return ResponseEntity.ok(response);
    }
}
