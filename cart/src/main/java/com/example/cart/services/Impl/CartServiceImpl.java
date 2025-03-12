package com.example.cart.services.Impl;

import com.example.cart.dto.ApiResponse;
import com.example.cart.dto.CartItemDto;
//import com.example.cart.dto.CartResponseDto;
import com.example.cart.entity.Cart;
import com.example.cart.entity.CartItem;
import com.example.cart.repository.CartRepository;
import com.example.cart.services.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

//    private void RecalculatePrice(Cart cart, List<CartItem> cartItemList) {
//        Double updatedTotalPrice = cartItemList.stream()
//                .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                .sum();
//
//        cart.setTotalPrice(updatedTotalPrice);
//    }

//    private CartResponseDto convertToCartResponse(Cart cart) {
//        List<CartItemDto> itemDtos = cart.getItems().stream()
//                .map(item -> new CartItemDto(
//                    item.getProductMerchantId(),
//                    item.getName(),
//                    item.getPrice(),
//                    item.getMerchantName(),
//                    item.getQuantity()
//                )).collect(Collectors.toList());
//        return new CartResponseDto(cart.getUserId(),itemDtos,cart.getTotalPrice());
//    }

    @Override
    //change user id to cart id maybe do in controller where you could access it
    public ApiResponse<Boolean> addToCart(String cartId, CartItemDto cartItemDto) {
        try {
            Cart cart = cartRepository.findById(cartId).get();
            log.info("User with id {} found cart", cartId);

            List<CartItem> currentCartItems = cart.getItems();
            log.info("Cart Item list is accessed");
            CartItem cartItem = new CartItem();

            cartItem.setProductMerchantId(cartItemDto.getProductMerchantId());
            cartItem.setName(cartItemDto.getName());
            cartItem.setPrice(cartItemDto.getPrice());
            cartItem.setMerchantName(cartItemDto.getMerchantName());
            cartItem.setQuantity(cartItemDto.getQuantity());
            cartItem.setImage(cartItemDto.getImage());

            currentCartItems.add(cartItem);
            
            cart.setItems(currentCartItems);
            log.info("Item added to Cart List");

            //RecalculatePrice(cart, cart.getItems());

            cartRepository.save(cart);
            log.info("Cart updated successfully in database");

            return new ApiResponse<>(HttpStatus.CREATED, "Item added to cart successfully", true);
        } catch (Exception e) {
            log.error("Error adding item to cart: {}", e.getMessage());
            return new ApiResponse<>(HttpStatus.CONFLICT, "Failed to add item to cart", false);
        }
    }

    @Override
    public ApiResponse<Boolean> removeFromCart(String cartId, String productMerchantId) {
        try{
            Cart cart = cartRepository.findById(cartId).get();
            boolean removed = cart.getItems().removeIf(item -> item.getProductMerchantId().equals(productMerchantId));

            if(removed) {
                cartRepository.save(cart);
                return new ApiResponse<>(HttpStatus.OK, "Item removed from cart successfully", true);
            } else {
                return new ApiResponse<>(HttpStatus.CONFLICT, "Item not removed from cart", false);
            }

        } catch (Exception e) {
                return new ApiResponse<>(HttpStatus.CONFLICT, "Issue when removing from the cart", false);
        }
    }

    @Override
    public ApiResponse<Long> updateQuantity(String cartId, String productMerchantId, Boolean increase) {
        try {
             Cart cart = cartRepository.findById(cartId).get();
//            .orElseThrow(() -> new RuntimeException("Cart not found"));
            List<CartItem> cartItemList = cart.getItems();
            CartItem cartItemToUpdate = cartItemList.stream()
                    .filter(item -> item.getProductMerchantId().equals(productMerchantId)).findAny().get();

//                    .orElseThrow(() -> new RuntimeException("Cart item not found"));

            if(cartItemToUpdate.getQuantity() >= 1) {
                cartItemToUpdate.setQuantity(cartItemToUpdate.getQuantity() + (increase ? 1 : -1));
            }

            cartRepository.save(cart);
            return new ApiResponse<>(HttpStatus.ACCEPTED, "Quantity updated successfully", cartItemToUpdate.getQuantity());

        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "Failed to update quantity", null);
        }
    }

    @Override
    public ApiResponse<List<CartItemDto>> getAllCartItems(String cartId) {
        Cart cart = cartRepository.findById(cartId).get();
        List<CartItem> cartItemList = cart.getItems();
        List<CartItemDto> cartItemDtoList = cartItemList.stream().map(item -> convertToCartItemDto(item))
                .collect(Collectors.toList());
        return new ApiResponse<>(HttpStatus.FOUND, "Fetching all the cart items", cartItemDtoList);
    }

    @Override
    public ApiResponse<Boolean> clearCart(String cartId) {
        try {
            Cart cart = cartRepository.findById(cartId).get();
            //handle total price
            cart.getItems().clear();
            cartRepository.save(cart);
            return new ApiResponse<>(HttpStatus.OK, "Cart cleared", true);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "Issue while clearing cart", false);
        }
    }

    @Override
    public ApiResponse<String> createEmptyCart(String userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cartRepository.save(cart);
        return new ApiResponse<>(HttpStatus.CREATED, "Created Empty card", cart.getId());
    }

    private CartItemDto convertToCartItemDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setProductMerchantId(cartItem.getProductMerchantId());
        cartItemDto.setName(cartItem.getName());
        cartItemDto.setPrice(cartItem.getPrice());
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setMerchantName(cartItem.getMerchantName());
        return cartItemDto;
    }


}
