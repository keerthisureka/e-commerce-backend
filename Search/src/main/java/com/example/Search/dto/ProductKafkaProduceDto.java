package com.example.Search.dto;

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
    private Double productMerchantPrice;
    private Long totalProductsOfferedByMerchant;
    private Long totalProductsSoldByMerchant;
    private Long productMerchantStock;
    private Double merchantRating;
    private Double productMerchantRating;
}
