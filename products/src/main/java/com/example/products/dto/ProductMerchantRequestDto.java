package com.example.products.dto;

import lombok.Data;

@Data
public class ProductMerchantRequestDto {
    private String merchantId;
    private Double price;
    private Long stock;
    private Double ratings;
}
