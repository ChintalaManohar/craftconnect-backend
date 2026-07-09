package com.craftconnect.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.craftconnect.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data

public class OrderResponse {

    private Long id;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;
    
    private List<OrderItemResponse> items;
    
    private String paymentMethod;

    private String paymentStatus;

    private String razorpayPaymentId;
    
    
    
    public OrderResponse(
            Long id,
            BigDecimal totalAmount,
            OrderStatus status,
            LocalDateTime createdAt,
            List<OrderItemResponse> items,
            String paymentMethod,
            String paymentStatus,
            String razorpayPaymentId) {

        this.id = id;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.razorpayPaymentId = razorpayPaymentId;
    }
    
}