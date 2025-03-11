package com.example.auth_server.service.impl;

import com.example.auth_server.dto.JWTRequest;
import com.example.auth_server.dto.JWTResponse;
import com.example.auth_server.dto.UserDTO;
import com.example.auth_server.entity.User;
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

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtUtils.generateToken(userDetails);

        JWTResponse response = new JWTResponse();
        response.setJwtToken(token);
        response.setUsername(userDetails.getUsername());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
        System.out.println(userDTO);
        User user = userService.createUser(userDTO);

        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getName());
        userDTO.setPassword(user.getPassword());

        return ResponseEntity.ok(userDTO);
    }
}
