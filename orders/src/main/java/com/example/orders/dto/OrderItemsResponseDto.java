package com.example.orders.dto;

import lombok.Data;

@Data
public class OrderItemsResponseDto {
    private String name;
    private Long quantity;
    private Double price;
}
