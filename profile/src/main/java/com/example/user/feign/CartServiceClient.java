package com.example.user.feign;

import com.example.user.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name="cart-service", url = "http://localhost:8084")
public interface CartServiceClient {

    @GetMapping("/Cart/getEmptyCartId/{userId}")
    public ApiResponse<String> getEmptyCartId(@PathVariable String userId);
}