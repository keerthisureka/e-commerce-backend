package com.example.Search.services;

import com.example.Search.dto.ProductKafkaProduceDto;

import java.util.List;

public interface ProductService {
    List<ProductKafkaProduceDto> searchProducts(String productName);
}
