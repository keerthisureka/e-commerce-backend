package com.example.auth_server.service.impl;

import com.example.auth_server.dto.UserDTO;
import com.example.auth_server.entity.User;
import com.example.auth_server.repositories.UserRepository;
import com.example.auth_server.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User createUser(UserDTO userDTO) {
        System.out.println(userDTO);
        User user = new User();
        user.setName(userDTO.getName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setUserId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

    public String getEmailByUserId(String userId) {
        User user = userRepository.findById(userId).get();
        return user.getEmail();
    }
}
