package com.example.cart.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Carts")
public class Cart {
    @Id
    private String id;
    private String userId;
    private List<CartItem> items;
    private Double totalPrice;
}
