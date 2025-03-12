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

    @PostMapping("/addToCart/{cartId}")
    public ResponseEntity<ApiResponse<Boolean>> addToCart(@PathVariable String cartId, @RequestBody CartItemDto cartItemDto) {
        return ResponseEntity.ok(cartService.addToCart(cartId, cartItemDto));
    }

    @DeleteMapping("/removeFromCart/{cartId}/{productMerchantId}")
    public ResponseEntity<ApiResponse<Boolean>> removeFromCart(@PathVariable String cartId, @PathVariable String productMerchantId) {
        return ResponseEntity.ok(cartService.removeFromCart(cartId,productMerchantId));
    }

    @PostMapping("/updateQuantity/{cartId}/{productMerchantId}/")
    public ResponseEntity<ApiResponse<Long>> updateQuantity(@PathVariable String cartId, @PathVariable String productMerchantId,@RequestParam Boolean increase) {
        return ResponseEntity.ok(cartService.updateQuantity(cartId, productMerchantId, increase));
    }

    @GetMapping("/getAllCartItems/{cartId}")
    public ResponseEntity<ApiResponse<List<CartItemDto>>> getAllCartItems(@PathVariable String cartId) {
        return ResponseEntity.ok(cartService.getAllCartItems(cartId));
    }

    @DeleteMapping("/clearCart/{cartId}")
    public ResponseEntity<ApiResponse<Boolean>> clearCart(@PathVariable String cartId){
        return ResponseEntity.ok(cartService.clearCart(cartId));
    }

    @GetMapping("/getEmptyCartId/{userId}")
    public ResponseEntity<ApiResponse<String>> getEmptyCartId(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.createEmptyCart(userId));
    }

}
