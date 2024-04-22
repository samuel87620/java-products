package com.example.demo.service.impl;

import com.example.demo.dto.OrderDto;
import com.example.demo.model.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final String productServiceUrl;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, RestTemplate restTemplate,
                            @Value("${product.service.url}") String productServiceUrl) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.productServiceUrl = productServiceUrl;
    }
    @Override
    @Transactional
    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        log.debug("Attempting to update order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
        if (orderDto.getId() != null && !order.getId().equals(orderDto.getId())) {
            throw new IllegalArgumentException("ID in DTO does not match entity ID");
        }
        if (orderDto.getProductId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        // 確保productId對應於數據庫中的一個有效產品ID
        // (這裡可能需要添加額外的邏輯來驗證產品ID的有效性)

        BeanUtils.copyProperties(orderDto, order, "id", "orderDate");
        order = orderRepository.saveAndFlush(order);
        log.debug("Order with ID: {} updated in database", order.getId());
        return convertToDto(order);
    }


    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        // 檢查產品是否存在於產品服務中
        if (!isProductExists(orderDto.getProductId())) {
            throw new RuntimeException("Product with ID: " + orderDto.getProductId() + " does not exist.");
        }

        Order order = convertToEntity(orderDto);
        order.setOrderDate(new Date()); // 設定訂單日期為當前時間
        order = orderRepository.save(order); // 保存訂單到數據庫
        return convertToDto(order); // 將保存的訂單轉換為DTO並返回
    }


    private boolean isProductExists(Long productId) {
        try {
            restTemplate.getForEntity(productServiceUrl + "/" + productId, String.class);
            return true; // 如果能正常返回，則表示產品存在
        } catch (Exception e) {
            return false; // 發生異常，則表示產品服務可能不存在此產品
        }
    }

    @Override
    @Transactional
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<OrderDto> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private OrderDto convertToDto(Order order) {
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(order, orderDto);
        return orderDto;
    }

    private Order convertToEntity(OrderDto orderDto) {
        Order order = new Order();
        // Copy properties but ignore the 'id' since it's auto-generated
        BeanUtils.copyProperties(orderDto, order, "id");
        return order;
    }
}