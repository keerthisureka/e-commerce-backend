package com.example.products.dto;

import lombok.Data;

@Data
public class ProductKafkaProduceDto {
    private String productId;
    private String productName;
    private String productImageUrl;
    private String productDescription;
    private String productUsp;
    private String merchantId;
    private String merchantName;
    private Double merchantPrice;
    private Double merchantScore;
}
