package com.example.cart.entity;

import lombok.Data;

@Data
public class CartItem {
    private String productMerchantId;
    private String image;
    private String name;
    private Double price;
    private String merchantName;
    private Long quantity;
}