package com.example.Search.services;

import com.example.Search.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    List<ProductResponseDto> searchProducts(String productName);
}
