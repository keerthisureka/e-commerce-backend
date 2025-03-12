package com.example.products.dto;

import lombok.Data;

@Data
public class ProductMerchantResponseDto {
    private String productMerchantId;
    private String merchantId;
    private String merchantName;
    private Double price;
    private Double score;
}
