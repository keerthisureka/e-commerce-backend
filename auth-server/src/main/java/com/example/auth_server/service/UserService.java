package com.example.auth_server.service;

import com.example.auth_server.dto.UserDTO;
import com.example.auth_server.entity.User;

import java.util.List;

public interface UserService {
    public User getUserDetails(String userId);
    public User createUser(UserDTO userDTO);
}
