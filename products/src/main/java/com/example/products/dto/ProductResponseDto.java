package com.example.products.dto;

import lombok.Data;
import java.util.List;


@Data
public class ProductResponseDto {
    private String id;

    private String name;

    private String imageUrl;

    private String description;

    private String usp;

    private List<ProductMerchantResponseDto> merchantList;
}
