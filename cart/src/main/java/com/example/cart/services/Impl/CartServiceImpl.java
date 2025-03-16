package com.example.cart.services.Impl;

import com.example.cart.dto.ApiResponse;
import com.example.cart.dto.CartItemDto;
//import com.example.cart.dto.CartResponseDto;
import com.example.cart.entity.Cart;
import com.example.cart.entity.CartItem;
import com.example.cart.feign.ProductServiceClient;
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

    @Autowired
    private ProductServiceClient productServiceClient;

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
    public ApiResponse<Boolean> addToCart(String userId, CartItemDto cartItemDto) {
        try {
            Cart cart = cartRepository.findByUserId(userId).orElse(new Cart());
            cart.setUserId(userId);

            List<CartItem> currentCartItems = cart.getItems();
            if (currentCartItems == null) {
                currentCartItems = new ArrayList<>();
            }

            // Check if the item is already in the cart
            CartItem existingItem = currentCartItems.stream()
                    .filter(item -> item.getProductMerchantId().equals(cartItemDto.getProductMerchantId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {
                // Update the quantity of the existing item
                existingItem.setQuantity(existingItem.getQuantity() + cartItemDto.getQuantity());
                Double currentTotal = cart.getTotalPrice() != null ? cart.getTotalPrice() : 0.0;
                cart.setTotalPrice(currentTotal + (cartItemDto.getPrice() * cartItemDto.getQuantity()));
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
            log.info("Updating quantity for userId: {}, productMerchantId: {}, increase: {}", userId, productMerchantId, increase);

            Cart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> {
                        log.error("Cart not found for userId: {}", userId);
                        return new RuntimeException("Cart not found");
                    });

            log.info("Cart retrieved successfully for userId: {}", userId);

            Optional<CartItem> optionalCartItem = cart.getItems().stream()
                    .filter(item -> item.getProductMerchantId().equals(productMerchantId))
                    .findFirst();

            if (!optionalCartItem.isPresent()) {
                log.error("Cart item not found for productMerchantId: {}", productMerchantId);
                return new ApiResponse<>(HttpStatus.NOT_FOUND, "Cart item not found", null);
            }

            CartItem cartItemToUpdate = optionalCartItem.get();
            long currentQuantity = cartItemToUpdate.getQuantity();
            long newQuantity = currentQuantity;

            log.info("Current quantity of productMerchantId {} is {}", productMerchantId, currentQuantity);

            if (!increase) {
                if (currentQuantity == 1) {
                    cart.getItems().remove(cartItemToUpdate);
                    newQuantity = 0;
                    log.info("Reduced quantity, cart item removed for productMerchantId {}", productMerchantId);
                } else {
                    newQuantity = newQuantity - 1;
                    log.info("Decreased quantity to {} for productMerchantId {}", newQuantity, productMerchantId);
                }
            } else {
                log.info("Fetching stock for productMerchantId {}", productMerchantId);
                Long stock = productServiceClient.getStock(productMerchantId).getData();
                log.info("Available stock for productMerchantId {} is {}", productMerchantId, stock);

                if (currentQuantity + 1 > stock) {
                    log.warn("Max stock reached for productMerchantId {}. Available: {}, Requested: {}", productMerchantId, stock, currentQuantity + 1);
                    return new ApiResponse<>(HttpStatus.BAD_REQUEST, "Max stock reached: " + stock + " units", -1L);
                }

                newQuantity = newQuantity + 1;
                log.info("Increased quantity to {} for productMerchantId {}", newQuantity, productMerchantId);
            }

            if (newQuantity > 0) {
                log.info("Updating cart item quantity in DB. New quantity: {}", newQuantity);
                cartItemToUpdate.setQuantity(newQuantity);
            }

            log.info("Calculating updated total price for cart");
            double updatedTotal = cart.getItems().stream()
                    .mapToDouble(item -> item.getPrice() * item.getQuantity())
                    .sum();
            cart.setTotalPrice(updatedTotal);

            log.info("Saving updated cart to database");
            cartRepository.save(cart);

            log.info("Quantity updated successfully for productMerchantId {}. New quantity: {}", productMerchantId, newQuantity);
            return new ApiResponse<>(HttpStatus.OK, "Quantity updated successfully", newQuantity);
        } catch (RuntimeException e) {
            log.error("RuntimeException occurred: {}", e.getMessage(), e);
            return new ApiResponse<>(HttpStatus.NOT_FOUND, e.getMessage(), null);
        } catch (Exception e) {
            log.error("Unexpected exception occurred while updating quantity", e);
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
