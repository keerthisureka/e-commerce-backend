package com.example.products.entity;

import com.example.products.dto.ProductMerchantResponseDto;
import com.example.products.dto.ProductResponseDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.PriorityQueue;

@Data
@Document(collection = "Products")
public class Product {
    @Id
    private String id;

    private String name;

    private String imageUrl;

    private String description;

    private String usp;

    private List<ProductMerchantResponseDto> merchantList;
}
