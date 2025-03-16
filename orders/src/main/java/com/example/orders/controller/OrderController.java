package com.example.orders.controller;

import com.example.orders.dto.ApiResponse;
import com.example.orders.dto.OrderHistoryResponseDto;
import com.example.orders.feign.CartServiceClient;
import com.example.orders.feign.ProductServiceClient;
import com.example.orders.services.Impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private ProductServiceClient productServiceClient;

    @PostMapping("/addToOrderHistory/{totalPrice}")
    public ResponseEntity<ApiResponse<Boolean>> addToOrderHistory(@RequestHeader("X-User-Id") String userId, @PathVariable Double totalPrice) {
        return ResponseEntity.ok(orderService.addOrder(userId, totalPrice));
    }

    @DeleteMapping("/clearCart")
    public ResponseEntity<ApiResponse<Boolean>> clearCart(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(cartServiceClient.clearCart(userId));
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<ApiResponse<List<OrderHistoryResponseDto>>> getAllOrders(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(orderService.getAllOrders(userId));
    }

    @GetMapping("/getEmptyOrderId")
    public ResponseEntity<ApiResponse<String>> getEmptyOrderHistoryId(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(orderService.createEmptyOrderHistory(userId));
    }
 }
