package com.example.user.feign;

import com.example.user.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="order-service", url = "http://localhost:8085")
public interface OrderServiceClient {

    @GetMapping("/Orders/getEmptyOrderId/{userId}")
    public ApiResponse<String> getEmptyOrderHistoryId(@PathVariable String userId);
}
