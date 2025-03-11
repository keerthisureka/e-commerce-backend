package com.example.user.controller;

import com.example.user.dto.ApiResponse;
import com.example.user.dto.UserRequestDto;
import com.example.user.services.Impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Boolean>> loginUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.loginUser(userRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Boolean>> registerUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.registerUser(userRequestDto));
    }
}
