package com.example.products.services;

import com.example.products.dto.ApiResponse;
import com.example.products.dto.ProductCardResponseDto;
import com.example.products.dto.ProductRequestDto;
import com.example.products.dto.ProductResponseDto;


import java.util.List;

public interface ProductService {
    public ApiResponse<Boolean> addProduct(List<ProductRequestDto> productRequestDto);

    public ApiResponse<List<ProductCardResponseDto>> getAllProducts();

    public ApiResponse<ProductResponseDto> getByProductId(String productId);

    public ApiResponse<String> getProductMerchantId(String productId, String merchantId);
}
