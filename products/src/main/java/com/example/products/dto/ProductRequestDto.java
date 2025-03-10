package com.example.products.dto;

import com.example.products.entity.Merchant;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequestDto {
    private String name;

    private String imageUrl;

    private String description;

    private String usp;

    private List<ProductMerchantRequestDto> productMerchantRequestDtoList;
}
