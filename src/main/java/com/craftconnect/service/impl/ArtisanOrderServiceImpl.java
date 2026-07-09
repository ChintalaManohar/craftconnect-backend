package com.craftconnect.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craftconnect.dto.ArtisanOrderResponse;
import com.craftconnect.entity.*;
import com.craftconnect.repository.*;
import com.craftconnect.service.ArtisanOrderService;

@Service
public class ArtisanOrderServiceImpl
        implements ArtisanOrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public ArtisanOrderServiceImpl(
            OrderItemRepository orderItemRepository,
            OrderRepository orderRepository,
            UserRepository userRepository) {

        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));
    }

    @Override
    public List<ArtisanOrderResponse> getArtisanOrders() {

        User artisan = getCurrentUser();

        return orderItemRepository
                .findByProductArtisanId(
                        artisan.getId())
                .stream()
                .map(item ->
                new ArtisanOrderResponse(
                        item.getOrder().getId(),
                        item.getOrder().getBuyer().getFullName(),
                        item.getProduct().getName(),
                        item.getProduct().getImageUrl(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getOrder().getStatus(),
                        item.getOrder().getCreatedAt()
                ))
                .toList();
    }

    @Override
    public String shipOrder(Long orderId) {

        User artisan = getCurrentUser();

        List<OrderItem> orderItems =
                orderItemRepository
                        .findByProductArtisanId(
                                artisan.getId());

        boolean ownsOrder =
                orderItems.stream()
                        .anyMatch(item ->
                                item.getOrder()
                                        .getId()
                                        .equals(orderId));

        if (!ownsOrder) {

            throw new RuntimeException(
                    "You can only ship your own orders");
        }

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order Not Found"));

        if (order.getStatus()
                != OrderStatus.CONFIRMED) {

            throw new RuntimeException(
                    "Only confirmed orders can be shipped");
        }

        order.setStatus(OrderStatus.SHIPPED);

        orderRepository.save(order);

        return "Order Shipped Successfully";
    }
}