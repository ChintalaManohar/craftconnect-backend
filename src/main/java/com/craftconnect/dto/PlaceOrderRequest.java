package com.craftconnect.dto;

import com.craftconnect.entity.PaymentMethod;

import lombok.Data;

@Data
public class PlaceOrderRequest {

    private PaymentMethod paymentMethod;

    // CART or BUY_NOW
    private String orderSource;

    // Required only for BUY_NOW
    private Long productId;

    private Integer quantity;
}