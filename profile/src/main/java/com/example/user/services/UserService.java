package com.example.user.services;

import com.example.user.dto.ApiResponse;
import com.example.user.dto.UserRequestDto;

public interface UserService {
    ApiResponse<Boolean> registerUser(UserRequestDto userRequestDto);

    ApiResponse<Boolean> loginUser(UserRequestDto userRequestDto);
}
