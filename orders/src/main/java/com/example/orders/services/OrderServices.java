package com.example.orders.services;

import com.example.orders.dto.ApiResponse;
import com.example.orders.dto.OrderHistoryResponseDto;

import java.util.List;

public interface OrderServices {
    ApiResponse<Boolean> addOrder(String userId, String cartId, Double totalPrice, String userEmail);

    ApiResponse<List<OrderHistoryResponseDto>> getAllOrders(String userId);

    ApiResponse<String> createEmptyOrderHistory(String userId);
}
