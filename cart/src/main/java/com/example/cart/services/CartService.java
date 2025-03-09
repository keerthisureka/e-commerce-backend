package com.example.cart.services;

import com.example.cart.dto.ApiResponse;
import com.example.cart.dto.CartItemDto;
import com.example.cart.entity.CartItem;

import java.util.List;

public interface CartService {
    //add a product to the cart
    ApiResponse<Boolean> addToCart(String userId, CartItemDto cartItemDto);
    //remove a prouct from the cart
    ApiResponse<Boolean> removeFromCart(String userId, String productMerchantId);
    //update a product quantity in the cart
    ApiResponse<Long> updateQuantity(String userId, String productMerchantId, Boolean increase);
    //get all details of the cart
    ApiResponse<List<CartItemDto>> getAllCartItems(String userId);

    ApiResponse<Boolean> clearCart(String userId);
}
