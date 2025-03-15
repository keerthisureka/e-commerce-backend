package com.example.cart.feign;


import com.example.cart.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8083")
public interface ProductServiceClient {

    @GetMapping("/Products/getStock/{productMerchantId}")
    public ApiResponse<Long> getStock(@PathVariable String productMerchantId);

}