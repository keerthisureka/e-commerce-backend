package com.example.products.services;

import com.example.products.dto.ApiResponse;
import com.example.products.dto.MerchantRequestDto;

import java.util.List;

public interface MerchantService {
    public ApiResponse<Boolean> addMerchant(List<MerchantRequestDto> merchantRequestDto);

    public ApiResponse<Boolean> updateMerchantStock(String merchantId, Long quantity);
}
