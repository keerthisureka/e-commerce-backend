package com.example.orders.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "http://localhost:8082")
public interface UserServiceClient {
    @GetMapping("/home/getEmailByUserId")
    public String getEmailByUserId(@RequestHeader("X-User-Id") String userId);
}
