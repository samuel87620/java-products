package com.example.demo.controller;

import com.example.demo.dto.OrderDto;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderViewController {

    private final OrderService orderService;

    @Autowired
    public OrderViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 返回所有订单的视图
    @GetMapping("/orders")
    public ModelAndView getAllOrdersView() {
        List<OrderDto> orders = orderService.getAllOrders();
        ModelAndView modelAndView = new ModelAndView("orders");
        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

    // 根据ID获取单个订单的视图
    @GetMapping("/{id}")
    public ModelAndView getOrderViewById(@PathVariable Long id) {
        OrderDto order = orderService.getOrderById(id);
        ModelAndView modelAndView = new ModelAndView("orders");
        modelAndView.addObject("order", order);
        return modelAndView;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        OrderDto newOrder = orderService.createOrder(orderDto);
        if (newOrder != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 创建新订单
    @PostMapping("")
    public String createOrder(@ModelAttribute OrderDto orderDto, RedirectAttributes redirectAttributes) {
        OrderDto newOrder = orderService.createOrder(orderDto);
        if (newOrder != null) {
            redirectAttributes.addFlashAttribute("success", "Order created successfully");
            return "redirect:/order/orders";
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to create order");
            return "redirect:/order/form"; // Assuming there's a form view for creating orders
        }
    }

    // 更新订单
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        OrderDto orderDto = orderService.getOrderById(id);
        if (orderDto != null) {
            model.addAttribute("order", orderDto);
            return "orders"; // 設定對應的視圖名稱
        } else {
            return "redirect:/order/orders"; // 如果訂單不存在，重定向回訂單列表
        }
    }



    // 删除订单
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isDeleted = orderService.deleteOrder(id);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("success", "Order deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to delete order");
        }
        return "redirect:/order/orders";
    }

    // 根据状态获取订单的JSON
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(@PathVariable String status) {
        List<OrderDto> orders = orderService.getOrdersByStatus(status);
        return !orders.isEmpty() ? ResponseEntity.ok(orders) : ResponseEntity.notFound().build();
    }
}
