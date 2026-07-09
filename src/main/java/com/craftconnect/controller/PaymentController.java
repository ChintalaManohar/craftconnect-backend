package com.craftconnect.controller;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.PaymentVerificationRequest;
import com.craftconnect.dto.RazorpayOrderResponse;
import com.craftconnect.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping("/create/{orderId}")
    public RazorpayOrderResponse createPayment(
            @PathVariable Long orderId) {

        return paymentService.createPayment(orderId);
    }
    @PostMapping("/verify")
    public String verifyPayment(
            @RequestBody
            PaymentVerificationRequest request) {

        return paymentService
                .verifyPayment(request);
    }
}