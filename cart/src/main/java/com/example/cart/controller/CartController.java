package com.example.cart.controller;

import com.example.cart.dto.ApiResponse;
import com.example.cart.dto.CartItemDto;
import com.example.cart.services.Impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartServiceImpl cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<ApiResponse<Boolean>> addToCart(@RequestHeader("X-User-Id") String userId, @RequestBody CartItemDto cartItemDto) {
        return ResponseEntity.ok(cartService.addToCart(userId, cartItemDto));
    }

    @DeleteMapping("/removeFromCart/{productMerchantId}")
    public ResponseEntity<ApiResponse<Boolean>> removeFromCart(@RequestHeader("X-User-Id") String userId, @PathVariable String productMerchantId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId,productMerchantId));
    }

    @PostMapping("/updateQuantity/{productMerchantId}")
    public ResponseEntity<ApiResponse<Long>> updateQuantity(@RequestHeader("X-User-Id") String userId, @PathVariable String productMerchantId,@RequestParam Boolean increase) {
        return ResponseEntity.ok(cartService.updateQuantity(userId, productMerchantId, increase));
    }

    @GetMapping("/getAllCartItems")
    public ResponseEntity<ApiResponse<List<CartItemDto>>> getAllCartItems(@RequestHeader("X-User-Id") String userId) {
        System.out.println(userId);
        return ResponseEntity.ok(cartService.getAllCartItems(userId));
    }

    @DeleteMapping("/clearCart")
    public ResponseEntity<ApiResponse<Boolean>> clearCart(@RequestHeader("X-User-Id") String userId){
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    @GetMapping("/getEmptyCartId")
    public ResponseEntity<ApiResponse<String>> getEmptyCartId(@RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(cartService.createEmptyCart(userId));
    }
}
