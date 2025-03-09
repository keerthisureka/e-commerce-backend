package com.example.orders.services;

import com.example.orders.dto.ApiResponse;
import com.example.orders.dto.OrderHistoryResponseDto;
import com.example.orders.entity.OrderItems;
import com.example.orders.entity.OrdersHistory;

import java.util.List;

public interface OrderServices {
    ApiResponse<Boolean> addOrder(String userId, Double totalPrice);

    ApiResponse<List<OrderHistoryResponseDto>> getAllOrders(String userId);
}
