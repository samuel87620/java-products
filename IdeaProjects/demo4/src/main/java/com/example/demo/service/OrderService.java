package com.example.demo.service;

import com.example.demo.dto.OrderDto;
import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(Long id);
    OrderDto createOrder(OrderDto orderDto);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    boolean deleteOrder(Long id);
    List<OrderDto> getOrdersByStatus(String status); // Make sure this method is declared
}