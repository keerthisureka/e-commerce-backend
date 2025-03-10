package com.example.products.dto;

import lombok.Data;

@Data
public class MerchantRequestDto {
    private String name;

    private Double ratings;

    private Long totalProductsListedByMerchant;

    private Long totalProductsSoldByMerchant;
}
