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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    @Transactional
    //change user id to cart id maybe do in controller where you could access it
    public ApiResponse<Boolean> addToCart(String userId, CartItemDto cartItemDto) {
        try {
            Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
            cart.setUserId(userId);

            log.info("User with id {} found cart", userId);

            List<CartItem> currentCartItems = cart.getItems();
            if (currentCartItems == null) {
                currentCartItems = new ArrayList<>();
            }

            log.info("Cart Item list is accessed");

            // Check if the item is already in the cart
            CartItem existingItem = currentCartItems.stream()
                    .filter(item -> item.getProductMerchantId().equals(cartItemDto.getProductMerchantId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                // Update the quantity of the existing item
                existingItem.setQuantity(existingItem.getQuantity() + cartItemDto.getQuantity());
                if (cart.getTotalPrice() == null) {
                    cart.setTotalPrice(0.0);
                }
                cart.setTotalPrice(cart.getTotalPrice() + (cartItemDto.getPrice() * cartItemDto.getQuantity()));
                log.info("Updated quantity of existing cart item");
            } else {
                // Create a new cart item
                CartItem cartItem = new CartItem();
                cartItem.setProductMerchantId(cartItemDto.getProductMerchantId());
                cartItem.setName(cartItemDto.getName());
                cartItem.setPrice(cartItemDto.getPrice());
                cartItem.setMerchantName(cartItemDto.getMerchantName());
                cartItem.setQuantity(cartItemDto.getQuantity());
                cartItem.setImage(cartItemDto.getImage());

                currentCartItems.add(cartItem);
                cart.setTotalPrice(cart.getTotalPrice() + (cartItemDto.getPrice() * cartItemDto.getQuantity()));
                log.info("Added new item to the cart");
            }

            cart.setItems(currentCartItems);

            cartRepository.save(cart);
            log.info("Cart updated successfully in database");

            return new ApiResponse<>(HttpStatus.CREATED, "Item added to cart successfully", true);
        } catch (Exception e) {
            log.error("Error adding item to cart: {}", e.getMessage());
            return new ApiResponse<>(HttpStatus.CONFLICT, "Failed to add item to cart", false);
        }
    }

    @Override
    @Transactional
    public ApiResponse<Boolean> removeFromCart(String userId, String productMerchantId) {
        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));
            CartItem itemToRemove = cart.getItems()
                    .stream()
                    .filter(item -> item.getProductMerchantId().equals(productMerchantId))
                    .findFirst()
                    .orElse(null);

            if (itemToRemove != null) {
                double itemTotalPrice = itemToRemove.getPrice() * itemToRemove.getQuantity(); // Store the item's total price
                cart.getItems().remove(itemToRemove);
                cart.setTotalPrice(cart.getTotalPrice() - itemTotalPrice);
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
    @Transactional
    public ApiResponse<Long> updateQuantity(String userId, String productMerchantId, Boolean increase) {
        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            Optional<CartItem> optionalCartItem = cart.getItems().stream()
                    .filter(item -> item.getProductMerchantId().equals(productMerchantId))
                    .findFirst();

            if (!optionalCartItem.isPresent()) {
                return new ApiResponse<>(HttpStatus.NOT_FOUND, "Cart item not found", null);
            }

            CartItem cartItemToUpdate = optionalCartItem.get();
            long currentQuantity = cartItemToUpdate.getQuantity();

            if (!increase && currentQuantity == 1) {
                cart.getItems().remove(cartItemToUpdate);
            } else {
                long newQuantity = currentQuantity + (increase ? 1 : -1);
                cartItemToUpdate.setQuantity(newQuantity);
            }

            double updatedTotal = cart.getItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
            cart.setTotalPrice(updatedTotal);

            cartRepository.save(cart);

            return new ApiResponse<>(HttpStatus.ACCEPTED, "Quantity updated successfully", increase ? currentQuantity + 1 : (currentQuantity == 1 ? 0 : currentQuantity - 1));
        } catch (RuntimeException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "Failed to update quantity", null);
        }
    }

    @Override
    public ApiResponse<List<CartItemDto>> getAllCartItems(String userId) {
        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            List<CartItemDto> cartItemDtoList = cart.getItems().stream()
                    .map(this::convertToCartItemDto)
                    .collect(Collectors.toList());

            return new ApiResponse<>(HttpStatus.OK, "Fetching all the cart items", cartItemDtoList);
        } catch (RuntimeException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "Failed to fetch cart items", null);
        }
    }

    @Override
    public ApiResponse<Boolean> clearCart(String userId) {
        try {
            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            // Clear all cart items and reset total price
            cart.getItems().clear();
            cart.setTotalPrice(0.0);
            cartRepository.save(cart);

            return new ApiResponse<>(HttpStatus.OK, "Cart cleared successfully", true);
        } catch (RuntimeException e) {
            return new ApiResponse<>(HttpStatus.NOT_FOUND, e.getMessage(), false);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "Issue while clearing cart", false);
        }
    }

    @Override
    public ApiResponse<String> createEmptyCart(String userId) {
        try {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>()); // Initialize empty item list
            cart.setTotalPrice(0.0); // Set total price to 0
            cartRepository.save(cart);
            return new ApiResponse<>(HttpStatus.CREATED, "Created Empty Cart", cart.getId());
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "Failed to create cart", null);
        }
    }

    private CartItemDto convertToCartItemDto(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getProductMerchantId(),
                cartItem.getName(),
                cartItem.getPrice(),
                cartItem.getMerchantName(),
                cartItem.getQuantity(),
                cartItem.getImage()
        );
    }
}
