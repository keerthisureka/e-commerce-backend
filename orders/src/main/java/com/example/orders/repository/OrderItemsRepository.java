package com.example.orders.repository;

import com.example.orders.entity.OrderItems;
import com.example.orders.entity.OrdersHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    List<OrderItems> findAllByOrdersHistory(OrdersHistory ordersHistory);
}
