package com.example.auth_server.service;

import com.example.auth_server.dto.UserDTO;
import com.example.auth_server.entity.User;

import java.util.List;

public interface UserService {
    public List<User> getAll();
    public String getEmailByUserId(String userId);
    public UserDTO getUserDetails(String userId);
}
