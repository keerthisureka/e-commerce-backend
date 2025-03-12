package com.example.cart.services;

import com.example.cart.dto.ApiResponse;
import com.example.cart.dto.CartItemDto;
import com.example.cart.entity.CartItem;

import java.util.List;

public interface CartService {
    //add a product to the cart
    ApiResponse<Boolean> addToCart(String cartId, CartItemDto cartItemDto);
    //remove a prouct from the cart
    ApiResponse<Boolean> removeFromCart(String cartId, String productMerchantId);
    //update a product quantity in the cart
    ApiResponse<Long> updateQuantity(String cartId, String productMerchantId, Boolean increase);
    //get all details of the cart
    ApiResponse<List<CartItemDto>> getAllCartItems(String cartId);

    ApiResponse<Boolean> clearCart(String cartId);

    ApiResponse<String> createEmptyCart(String userId);
}
