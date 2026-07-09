package com.craftconnect.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.craftconnect.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminOrderResponse {

    private Long orderId;

    private Long buyerId;

    private String buyerName;

    private String buyerEmail;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private String paymentMethod;

    private String paymentStatus;

    private String razorpayPaymentId;

    private List<AdminOrderItemResponse> items;
}