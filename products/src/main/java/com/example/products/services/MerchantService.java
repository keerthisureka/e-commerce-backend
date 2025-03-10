package com.example.products.services;

import com.example.products.dto.ApiResponse;
import com.example.products.dto.MerchantRequestDto;

public interface MerchantService {
    public ApiResponse<Boolean> addMerchant(MerchantRequestDto merchantRequestDto);
}
