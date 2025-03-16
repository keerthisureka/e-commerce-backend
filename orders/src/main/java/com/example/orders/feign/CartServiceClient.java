package com.example.orders.feign;

import com.example.orders.dto.ApiResponse;
import com.example.orders.dto.CartItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="cart-service", url = "http://localhost:8084")
public interface CartServiceClient {
    @GetMapping("/Cart/getAllCartItems")
    ApiResponse<List<CartItemDto>> getAllCartItems(@RequestHeader("X-User-Id") String userId);

    @DeleteMapping("/Cart/clearCart")
    ApiResponse<Boolean> clearCart(@RequestHeader("X-User-Id") String userId);
}
