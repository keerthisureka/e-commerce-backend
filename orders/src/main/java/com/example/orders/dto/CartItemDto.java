package com.example.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private String productMerchantId;
    private String name;
    private Double price;
    private String merchantName;
    private Long quantity;
}
