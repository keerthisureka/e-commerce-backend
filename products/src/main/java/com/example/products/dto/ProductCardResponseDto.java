package com.example.products.dto;

import lombok.Data;

@Data
public class ProductCardResponseDto {
    private String productId;
    private String image;
    private String name;
    private Double price;
    private String description;
    private String usp;
}
