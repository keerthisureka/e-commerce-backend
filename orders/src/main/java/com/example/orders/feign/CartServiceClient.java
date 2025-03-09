package com.example.orders.feign;

import com.example.orders.dto.ApiResponse;
import com.example.orders.dto.CartItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="cart-service", url = "http://localhost:8084")
public interface CartServiceClient {

    @GetMapping("/Cart/getAllCartItems/{userId}")
    ApiResponse<List<CartItemDto>> getAllCartItems(@PathVariable String userId);

    @DeleteMapping("/Cart/clearCart/{userId}")
    ApiResponse<Boolean> clearCart(@PathVariable String userId);
}
