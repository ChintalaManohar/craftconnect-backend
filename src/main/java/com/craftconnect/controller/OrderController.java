package com.craftconnect.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.OrderResponse;
import com.craftconnect.dto.PlaceOrderRequest;
import com.craftconnect.entity.Order;
import com.craftconnect.entity.OrderStatus;
import com.craftconnect.entity.PaymentMethod;
import com.craftconnect.entity.User;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    
    
    
    
    

    public OrderController(
            OrderService orderService ) {

        this.orderService = orderService;
        
    }

    @PostMapping
    public OrderResponse placeOrder(
            @RequestBody PlaceOrderRequest request) {

        return orderService.placeOrder(request);
    }

    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders() {

        return orderService.getMyOrders();
    }
    @PutMapping("/{orderId}/deliver")
    public String markAsDelivered(
            @PathVariable Long orderId) {

        return orderService
                .markAsDelivered(orderId);
    }
    @PutMapping("/{orderId}/cancel")
    public String cancelOrder(
            @PathVariable Long orderId) {

        return orderService
                .cancelOrder(orderId);
    }
    
}