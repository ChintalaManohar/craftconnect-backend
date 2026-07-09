package com.craftconnect.service;

import com.craftconnect.dto.PaymentVerificationRequest;
import com.craftconnect.dto.RazorpayOrderResponse;

public interface PaymentService {

    RazorpayOrderResponse createPayment(Long orderId);

    String verifyPayment(
            PaymentVerificationRequest request);
}