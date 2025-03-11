package com.example.products.services.Impl;

import com.example.products.dto.ApiResponse;
import com.example.products.dto.MerchantRequestDto;
import com.example.products.entity.Merchant;
import com.example.products.entity.ProductMerchant;
import com.example.products.repository.MerchantRepository;
import com.example.products.repository.ProductMerchantRepository;
import com.example.products.services.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private ProductMerchantRepository productMerchantRepository;


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

    @Override
    public ApiResponse<Boolean> updateMerchantStock(String productMerchantId, Long quantity) {
        ProductMerchant productMerchant = productMerchantRepository.findById(productMerchantId).get();
        Long currentStock = productMerchant.getStock();
        if(currentStock - quantity > 0) {
            productMerchant.setStock(currentStock - quantity);
        } else {
            return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Out Of Stock", false);
        }
        productMerchantRepository.save(productMerchant);
        Merchant merchant = merchantRepository.findById(productMerchant.getMerchantId()).get();
        merchant.setTotalProductsSoldByMerchant( merchant.getTotalProductsSoldByMerchant() + quantity);
        merchantRepository.save(merchant);
        return new ApiResponse<>(HttpStatus.OK, "Updated Merchant stocks with quantity", true);
    }
}
