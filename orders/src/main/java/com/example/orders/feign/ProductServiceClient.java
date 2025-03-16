package com.example.orders.feign;

import com.example.orders.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "product-service", url = "http://localhost:8083")
public interface ProductServiceClient {
    @PostMapping("/Merchant/updateMerchantStock/{productMerchantId}/{quantity}")
    public ApiResponse<Boolean> updateMerchantStock(@PathVariable String productMerchantId, @PathVariable Long quantity);
}
