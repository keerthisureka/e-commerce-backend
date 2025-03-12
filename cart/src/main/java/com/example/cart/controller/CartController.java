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

    @PostMapping("/addToCart/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> addToCart(@PathVariable String userId, @RequestBody CartItemDto cartItemDto) {
        return ResponseEntity.ok(cartService.addToCart(userId, cartItemDto));
    }

    @DeleteMapping("/removeFromCart/{userId}/{productMerchantId}")
    public ResponseEntity<ApiResponse<Boolean>> removeFromCart(@PathVariable String userId, @PathVariable String productMerchantId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId,productMerchantId));
    }

    @PostMapping("/updateQuantity/{userId}/{productMerchantId}/")
    public ResponseEntity<ApiResponse<Long>> updateQuantity(@PathVariable String userId, @PathVariable String productMerchantId,@RequestParam Boolean increase) {
        return ResponseEntity.ok(cartService.updateQuantity(userId, productMerchantId, increase));
    }

    @GetMapping("/getAllCartItems/{userId}")
    public ResponseEntity<ApiResponse<List<CartItemDto>>> getAllCartItems(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getAllCartItems(userId));
    }

    @DeleteMapping("/clearCart/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> clearCart(@PathVariable String userId){
        return ResponseEntity.ok(cartService.clearCart(userId));
    }

    @GetMapping("/getEmptyCartId/{userId}")
    public ResponseEntity<ApiResponse<String>> getEmptyCartId(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.createEmptyCart(userId));
    }
}
