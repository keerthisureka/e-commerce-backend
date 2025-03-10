package com.example.products.services.Impl;

import com.example.products.dto.ApiResponse;
import com.example.products.dto.MerchantRequestDto;
import com.example.products.entity.Merchant;
import com.example.products.repository.MerchantRepository;
import com.example.products.services.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;


    @Override
    public ApiResponse<Boolean> addMerchant(MerchantRequestDto merchantRequestDto) {
        try {
            Merchant merchant = new Merchant();
            merchant.setName(merchantRequestDto.getName());
            merchant.setRatings(merchantRequestDto.getRatings());
            merchantRepository.save(merchant);
            return new ApiResponse<>(HttpStatus.CREATED, "merchant created successfully", true);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, e.getMessage(), false);
        }
    }
}
