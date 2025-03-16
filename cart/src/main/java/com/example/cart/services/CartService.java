package com.example.cart.services;

import com.example.cart.dto.ApiResponse;
import com.example.cart.dto.CartItemDto;
import com.example.cart.entity.CartItem;

import java.util.List;

public interface CartService {

    ApiResponse<Boolean> addToCart(String userId, CartItemDto cartItemDto);

    ApiResponse<Boolean> removeFromCart(String userId, String productMerchantId);

    ApiResponse<Long> updateQuantity(String userId, String productMerchantId, Boolean increase);

    ApiResponse<List<CartItemDto>> getAllCartItems(String userId);

    ApiResponse<Boolean> clearCart(String userId);

    ApiResponse<String> createEmptyCart(String userId);
}
