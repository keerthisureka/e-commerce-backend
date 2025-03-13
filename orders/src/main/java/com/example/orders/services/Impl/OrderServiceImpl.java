package com.example.orders.services.Impl;

import com.example.orders.dto.ApiResponse;
import com.example.orders.dto.CartItemDto;
import com.example.orders.dto.OrderHistoryResponseDto;
import com.example.orders.dto.OrderItemsResponseDto;
import com.example.orders.entity.OrderItems;
import com.example.orders.entity.OrdersHistory;
import com.example.orders.feign.CartServiceClient;
import com.example.orders.feign.ProductServiceClient;
import com.example.orders.feign.UserServiceClient;
import com.example.orders.repository.OrderHistoryRepository;
import com.example.orders.repository.OrderItemsRepository;
import com.example.orders.services.OrderServices;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl implements OrderServices {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Autowired
    private CartServiceClient cartServiceClient;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private UserServiceClient userServiceClients;

    @Autowired
    private EmailServiceImpl emailService;



    @Override
    public ApiResponse<Boolean> addOrder(String userId, Double totalPrice) {
        try {
            ApiResponse<List<CartItemDto>> currentCartItemsResponse = cartServiceClient.getAllCartItems(userId);
            List<CartItemDto> currentCartItems = currentCartItemsResponse.getData();

            List<OrderItems> items = new ArrayList<>();

            OrdersHistory ordersHistory = new OrdersHistory();
            ordersHistory.setTotalAmount(totalPrice);
            ordersHistory.setUserId(userId);
            for (CartItemDto cartItemDto : currentCartItems) {
                OrderItems orderItems = new OrderItems();
                orderItems.setProductMerchantId(cartItemDto.getProductMerchantId());
                orderItems.setName(cartItemDto.getName());
                orderItems.setPrice(cartItemDto.getPrice());
                orderItems.setQuantity(cartItemDto.getQuantity());
                orderItems.setOrdersHistory(ordersHistory);
                items.add(orderItems);
                productServiceClient.updateMerchantStock(cartItemDto.getProductMerchantId(), cartItemDto.getQuantity());
            }

            ordersHistory.setItems(items);
            orderHistoryRepository.save(ordersHistory);

            try{
                String userEmail = userServiceClients.getEmailByUserId(userId);
                emailService.sendOrderConfirmation(userEmail, ordersHistory.getId());
            } catch (Exception e) {
                log.info(e.getMessage());
            }

            return new ApiResponse<>(HttpStatus.CREATED, "Added to Order history", true);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "Issue while adding order to history", false);
        }
    }

    @Override
    public ApiResponse<List<OrderHistoryResponseDto>> getAllOrders(String userId) {
        try {
            List<OrdersHistory> allOrdersByUserId = orderHistoryRepository.findAllByUserId(userId);
            if( allOrdersByUserId.isEmpty()) {
                return new ApiResponse<>(HttpStatus.BAD_REQUEST, "No orders found for user in " + userId, null);
            }
            
            List<OrderHistoryResponseDto> allOrders = new ArrayList<>();
            
            for(OrdersHistory ordersHistory: allOrdersByUserId) {
                String orderId = ordersHistory.getId();

                List<OrderItems> allItemsOfOrder = orderItemsRepository.findAllByOrdersHistory(ordersHistory);

                OrderHistoryResponseDto orderHistoryResponseDto = getOrderHistoryResponseDto(allItemsOfOrder, orderId);

                allOrders.add(orderHistoryResponseDto);
            }
            return new ApiResponse<>(HttpStatus.FOUND, "successfully got the order details", allOrders);
        } catch (Exception e) {
            return new ApiResponse<>(HttpStatus.CONFLICT, "Issue while fetching details", null);
        }
    }

    @Override
    public ApiResponse<String> createEmptyOrderHistory(String userId) {
        OrdersHistory order = new OrdersHistory();
        order.setUserId(userId);
        orderHistoryRepository.save(order);
        return new ApiResponse<>(HttpStatus.CREATED, " empty order history created", order.getId());

    }

    private OrderHistoryResponseDto getOrderHistoryResponseDto(List<OrderItems> allItemsOfOrder, String orderId) {
        List<OrderItemsResponseDto> allOrderItems = new ArrayList<>();

        for(OrderItems orderItems : allItemsOfOrder) {
            OrderItemsResponseDto orderItemsResponseDto = new OrderItemsResponseDto();
            orderItemsResponseDto.setName(orderItems.getName());
            orderItemsResponseDto.setPrice(orderItems.getPrice());
            orderItemsResponseDto.setQuantity(orderItems.getQuantity());
            allOrderItems.add(orderItemsResponseDto);
        }

        OrderHistoryResponseDto orderHistoryResponseDto = new OrderHistoryResponseDto();
        orderHistoryResponseDto.setId(orderId);
        orderHistoryResponseDto.setOrderItemsResponseDtoList(allOrderItems);
        orderHistoryResponseDto.setTotalPrice(orderHistoryRepository.findById(orderId).get().getTotalAmount());
        return orderHistoryResponseDto;
    }
}
