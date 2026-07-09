package com.craftconnect.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craftconnect.dto.RazorpayOrderResponse;
import com.craftconnect.entity.Cart;
import com.craftconnect.entity.CartItem;
import com.craftconnect.entity.Order;
import com.craftconnect.entity.OrderItem;
import com.craftconnect.entity.Payment;
import com.craftconnect.entity.PaymentStatus;
import com.craftconnect.entity.User;
import com.craftconnect.repository.CartItemRepository;
import com.craftconnect.repository.CartRepository;
import com.craftconnect.repository.OrderItemRepository;
import com.craftconnect.repository.OrderRepository;
import com.craftconnect.repository.PaymentRepository;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.service.PaymentService;
import com.razorpay.RazorpayClient;
import com.craftconnect.dto.PaymentVerificationRequest;
import com.craftconnect.entity.OrderStatus;

@Service
public class PaymentServiceImpl
        implements PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    
    
    
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));
    }

    public PaymentServiceImpl(
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.orderItemRepository= orderItemRepository;
    }
    @Override
    public RazorpayOrderResponse createPayment(
            Long orderId) {

        try {

            Order order =
                    orderRepository.findById(orderId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Order Not Found"));

            RazorpayClient client =
                    new RazorpayClient(
                            keyId,
                            keySecret);

            JSONObject options =
                    new JSONObject();

            options.put(
                    "amount",
                    order.getTotalAmount()
                            .multiply(
                                    BigDecimal.valueOf(100))
                            .longValue());

            options.put(
                    "currency",
                    "INR");

            options.put(
                    "receipt",
                    "order_" + order.getId());

            com.razorpay.Order razorpayOrder =
                    client.orders.create(options);

            Payment payment =
                    new Payment();

            payment.setOrder(order);

            payment.setAmount(
                    order.getTotalAmount());

            payment.setRazorpayOrderId(
                    razorpayOrder.get("id"));

            payment.setStatus(
                    PaymentStatus.PENDING);

            paymentRepository.save(payment);

            return new RazorpayOrderResponse(
                    razorpayOrder.get("id"),
                    keyId,
                    order.getId(),
                    order.getTotalAmount()
                            .multiply(
                                    BigDecimal.valueOf(100))
                            .longValue());

        } catch (Exception e) {

            throw new RuntimeException(
                    e.getMessage());
        }
        
        }
    @Override
    public String verifyPayment(
            PaymentVerificationRequest request) {

        Payment payment =
                paymentRepository
                        .findByRazorpayOrderId(
                                request.getRazorpayOrderId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment Not Found"));

        payment.setRazorpayPaymentId(
                request.getRazorpayPaymentId());

        payment.setRazorpaySignature(
                request.getRazorpaySignature());

        payment.setStatus(
                PaymentStatus.SUCCESS);

        paymentRepository.save(payment);

        Order order = payment.getOrder();

        order.setStatus(
                OrderStatus.CONFIRMED);

        orderRepository.save(order);
        
        User buyer = getCurrentUser();

        Cart cart = cartRepository
                .findByBuyerId(order.getBuyer().getId())
                .orElse(null);

        if (cart != null) {

            List<CartItem> cartItems =
                    cartItemRepository.findByCartId(cart.getId());

            List<OrderItem> orderItems =
                    orderItemRepository.findByOrderId(order.getId());

            for (OrderItem orderItem : orderItems) {

                CartItem matchingCartItem =
                        cartItems.stream()
                                .filter(cartItem ->
                                        cartItem.getProduct()
                                                .getId()
                                                .equals(
                                                        orderItem
                                                                .getProduct()
                                                                .getId()))
                                .findFirst()
                                .orElse(null);

                if (matchingCartItem != null) {

                    /*
                     * Example:
                     *
                     * Cart currently has quantity = 5
                     * Paid order quantity = 2
                     *
                     * Remaining cart quantity = 3
                     */

                    int remainingQuantity =
                            matchingCartItem.getQuantity()
                                    - orderItem.getQuantity();

                    if (remainingQuantity <= 0) {

                        // Paid quantity covers the entire cart item
                        cartItemRepository.delete(
                                matchingCartItem);

                    } else {

                        // Buyer still has unpaid quantity remaining
                        matchingCartItem.setQuantity(
                                remainingQuantity);

                        cartItemRepository.save(
                                matchingCartItem);
                    }
                }
            }
        }

        return "Payment Verified Successfully";
    }
}