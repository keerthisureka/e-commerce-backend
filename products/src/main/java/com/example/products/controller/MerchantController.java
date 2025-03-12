package com.example.products.controller;

import com.example.products.dto.ApiResponse;
import com.example.products.dto.MerchantRequestDto;
import com.example.products.services.Impl.MerchantServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Merchant")
@CrossOrigin(origins = "*")
public class MerchantController {

    @Autowired
    private MerchantServiceImpl merchantService;

    @PostMapping("/addMerchant")
    public ResponseEntity<ApiResponse<Boolean>> addMerchant(@RequestBody List<MerchantRequestDto> merchantRequestDto) {
        return ResponseEntity.ok(merchantService.addMerchant(merchantRequestDto));
    }

    @PostMapping("/updateMerchantStock/{productMerchantId}/{quantity}")
    public ResponseEntity<ApiResponse<Boolean>> updateMerchantStock(@PathVariable String productMerchantId, @PathVariable Long quantity) {
        return ResponseEntity.ok(merchantService.updateMerchantStock(productMerchantId, quantity));
    }

}
