package com.example.user.services.Impl;

import com.example.user.dto.ApiResponse;
import com.example.user.dto.UserRequestDto;
import com.example.user.entity.Profile;
import com.example.user.entity.ProfileOrderCart;
import com.example.user.feign.CartServiceClient;
import com.example.user.feign.OrderServiceClient;
import com.example.user.repository.ProfileOrderCartRepository;
import com.example.user.repository.UserRepository;
import com.example.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileOrderCartRepository profileOrderCartRepository;

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Override
    public ApiResponse<Boolean> registerUser(UserRequestDto userRequestDto) {
        Profile user = new Profile();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(userRequestDto.getPassword());
        userRepository.save(user);

        ProfileOrderCart profileOrderCart = new ProfileOrderCart();
        profileOrderCart.setUserId(user.getId());
        profileOrderCart.setCartId(cartServiceClient.getEmptyCartId(user.getId()).getData());
        profileOrderCart.setOrderId(orderServiceClient.getEmptyOrderHistoryId(user.getId()).getData());
        profileOrderCartRepository.save(profileOrderCart);

        return new ApiResponse<> (HttpStatus.OK, "User registered successfully", true);
    }

    @Override
    public ApiResponse<Boolean> loginUser(UserRequestDto userRequestDto) {
        String incomingEmail = userRequestDto.getEmail();
        Profile user = userRepository.findByEmail(incomingEmail);

        if (user == null) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, "User does not exist", false);
        }

        if (!userRequestDto.getPassword().equals(user.getPassword())) {
            return new ApiResponse<>(HttpStatus.UNAUTHORIZED, "Wrong password", false);
        }
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return new ApiResponse<>(HttpStatus.OK, "User Logged in successfully", true);
    }
}
