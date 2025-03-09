package com.example.orders.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@ToString(exclude = "items")
public class OrdersHistory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String userId;

    private Double totalAmount;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "ordersHistory", cascade = CascadeType.ALL)
    private List<OrderItems> items;
}
