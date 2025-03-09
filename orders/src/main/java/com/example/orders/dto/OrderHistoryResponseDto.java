package com.example.orders.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderHistoryResponseDto {
    private String id; //order id
    private List<OrderItemsResponseDto> orderItemsResponseDtoList;
}
