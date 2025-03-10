package com.example.products.controller;

import com.example.products.dto.ApiResponse;
import com.example.products.dto.MerchantRequestDto;
import com.example.products.services.Impl.MerchantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Merchant")
public class MerchantController {

    @Autowired
    private MerchantServiceImpl merchantService;

    @PostMapping("/addMerchant")
    public ResponseEntity<ApiResponse<Boolean>> addMerchant(@RequestBody MerchantRequestDto merchantRequestDto) {
        return ResponseEntity.ok(merchantService.addMerchant(merchantRequestDto));
    }
}
