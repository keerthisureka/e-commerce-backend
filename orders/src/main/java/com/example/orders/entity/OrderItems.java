package com.example.orders.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String productMerchantId;
    private String name;
    private Double price;

    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private OrdersHistory ordersHistory;

}
