package com.craftconnect.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.craftconnect.dto.OrderItemResponse;
import com.craftconnect.dto.OrderResponse;
import com.craftconnect.dto.PlaceOrderRequest;
import com.craftconnect.entity.*;
import com.craftconnect.repository.*;
import com.craftconnect.service.OrderService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final ProductRepository productRepository;
    
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

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,PaymentRepository paymentRepository,ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.paymentRepository= paymentRepository;
        this.productRepository =productRepository;
    }

    @Override
    public OrderResponse placeOrder(
            PlaceOrderRequest request) {

        User buyer = getCurrentUser();

        if (request.getPaymentMethod() == null) {
            throw new RuntimeException(
                    "Payment method is required");
        }

        if (request.getOrderSource() == null) {
            throw new RuntimeException(
                    "Order source is required");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        Order order = new Order();

        order.setBuyer(buyer);

        order.setPaymentMethod(
                request.getPaymentMethod());

        if (request.getPaymentMethod()
                == PaymentMethod.COD) {

            order.setStatus(OrderStatus.CONFIRMED);

        } else {

            order.setStatus(
                    OrderStatus.PENDING_PAYMENT);
        }

        /*
         * =========================
         * BUY NOW
         * =========================
         */

        if ("BUY_NOW".equalsIgnoreCase(
                request.getOrderSource())) {

            if (request.getProductId() == null) {
                throw new RuntimeException(
                        "Product ID is required");
            }

            if (request.getQuantity() == null
                    || request.getQuantity() <= 0) {

                throw new RuntimeException(
                        "Valid quantity is required");
            }

            Product product =
                    productRepository
                            .findById(request.getProductId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product Not Found"));

            if (!Boolean.TRUE.equals(
                    product.getActive())) {

                throw new RuntimeException(
                        "Product is not available");
            }

            if (product.getQuantity()
                    < request.getQuantity()) {

                throw new RuntimeException(
                        "Insufficient product quantity");
            }

            totalAmount =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            request.getQuantity()));

            order.setTotalAmount(totalAmount);

            Order savedOrder =
                    orderRepository.save(order);

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(savedOrder);

            orderItem.setProduct(product);

            orderItem.setQuantity(
                    request.getQuantity());

            orderItem.setPrice(
                    product.getPrice());

            orderItemRepository.save(orderItem);

            List<OrderItemResponse> items =
                    List.of(
                            new OrderItemResponse(

                                    product.getId(),

                                    product.getName(),

                                    product.getImageUrl(),

                                    request.getQuantity(),

                                    product.getPrice(),

                                    product.getArtisan()
                                            .getFullName()
                            )
                    );

            return new OrderResponse(
                    savedOrder.getId(),
                    savedOrder.getTotalAmount(),
                    savedOrder.getStatus(),
                    savedOrder.getCreatedAt(),
                    items,
                    savedOrder
                            .getPaymentMethod()
                            .name(),
                    "PENDING",
                    null
            );
        }

        /*
         * =========================
         * CART CHECKOUT
         * =========================
         */

        if ("CART".equalsIgnoreCase(
                request.getOrderSource())) {

            Cart cart =
                    cartRepository
                            .findByBuyerId(buyer.getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Cart Not Found"));

            List<CartItem> cartItems =
                    cartItemRepository
                            .findByCartId(cart.getId());

            if (cartItems.isEmpty()) {
                throw new RuntimeException(
                        "Cart Is Empty");
            }

            for (CartItem item : cartItems) {

                if (!Boolean.TRUE.equals(
                        item.getProduct().getActive())) {

                    throw new RuntimeException(
                            item.getProduct().getName()
                                    + " is not available");
                }

                if (item.getProduct().getQuantity()
                        < item.getQuantity()) {

                    throw new RuntimeException(
                            "Insufficient quantity for "
                                    + item.getProduct().getName());
                }

                BigDecimal itemTotal =
                        item.getProduct()
                                .getPrice()
                                .multiply(
                                        BigDecimal.valueOf(
                                                item.getQuantity()));

                totalAmount =
                        totalAmount.add(itemTotal);
            }

            order.setTotalAmount(totalAmount);

            Order savedOrder =
                    orderRepository.save(order);

            for (CartItem item : cartItems) {

                OrderItem orderItem =
                        new OrderItem();

                orderItem.setOrder(savedOrder);

                orderItem.setProduct(
                        item.getProduct());

                orderItem.setQuantity(
                        item.getQuantity());

                orderItem.setPrice(
                        item.getProduct()
                                .getPrice());

                orderItemRepository.save(orderItem);
            }

            List<OrderItemResponse> items =
                    orderItemRepository
                            .findByOrderId(
                                    savedOrder.getId())
                            .stream()
                            .map(item ->
                                    new OrderItemResponse(

                                            item.getProduct()
                                                    .getId(),

                                            item.getProduct()
                                                    .getName(),

                                            item.getProduct()
                                                    .getImageUrl(),

                                            item.getQuantity(),

                                            item.getPrice(),

                                            item.getProduct()
                                                    .getArtisan()
                                                    .getFullName()
                                    ))
                            .toList();

            /*
             * COD cart checkout:
             * clear cart immediately.
             *
             * ONLINE:
             * cart will be modified after
             * successful payment verification.
             */

            if (request.getPaymentMethod()
                    == PaymentMethod.COD) {

                cartItemRepository
                        .deleteAll(cartItems);
            }

            return new OrderResponse(
                    savedOrder.getId(),
                    savedOrder.getTotalAmount(),
                    savedOrder.getStatus(),
                    savedOrder.getCreatedAt(),
                    items,
                    savedOrder
                            .getPaymentMethod()
                            .name(),
                    "PENDING",
                    null
            );
        }

        throw new RuntimeException(
                "Invalid order source. Use CART or BUY_NOW");
    }

    @Override
    public List<OrderResponse> getMyOrders() {

        User buyer = getCurrentUser();
        
        

        return orderRepository
                .findByBuyerId(buyer.getId())
                .stream()
                .map(order -> {

                    List<OrderItemResponse> items =
                            orderItemRepository
                                    .findByOrderId(order.getId())
                                    .stream()
                                    .map(item -> new OrderItemResponse(
                                            item.getProduct().getId(),
                                            item.getProduct().getName(),
                                            item.getProduct().getImageUrl(),
                                            item.getQuantity(),
                                            item.getPrice(),
                                            item.getProduct().getArtisan().getFullName()
                                    ))
                                    .toList();
                    
                    Payment payment =
                            paymentRepository
                                    .findByOrderId(order.getId())
                                    .orElse(null);
                    
     

                    return new OrderResponse(
                            order.getId(),
                            order.getTotalAmount(),
                            order.getStatus(),
                            order.getCreatedAt(),
                            items,

                            order.getPaymentMethod().name(),

                            order.getPaymentMethod() == PaymentMethod.COD
                                    ? "PENDING"
                                    : payment == null
                                        ? "PENDING"
                                        : payment.getStatus().name(),

                            payment == null
                                    ? null
                                    : payment.getRazorpayPaymentId()
                    );
                })
                .toList();
    }
    @Override
    public String markAsDelivered(Long orderId) {

        User buyer = getCurrentUser();

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order Not Found"));

        if (!order.getBuyer()
                .getId()
                .equals(buyer.getId())) {

            throw new RuntimeException(
                    "You can only update your own orders");
        }

        if (order.getStatus()
                != OrderStatus.SHIPPED) {

            throw new RuntimeException(
                    "Only shipped orders can be delivered");
        }

        order.setStatus(
                OrderStatus.DELIVERED);

        orderRepository.save(order);

        return "Order Delivered Successfully";
    }
    @Override
    public String cancelOrder(Long orderId) {

        User buyer = getCurrentUser();

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order Not Found"));

        if (!order.getBuyer()
                .getId()
                .equals(buyer.getId())) {

            throw new RuntimeException(
                    "You can only cancel your own orders");
        }

        if (order.getStatus() == OrderStatus.SHIPPED
                || order.getStatus() == OrderStatus.DELIVERED) {

            throw new RuntimeException(
                    "Order cannot be cancelled");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {

            throw new RuntimeException(
                    "Order already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        return "Order Cancelled Successfully";
    }
   
}