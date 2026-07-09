package com.craftconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RazorpayOrderResponse {

    private String razorpayOrderId;

    private String key;

    private Long orderId;

    private Long amount;
}