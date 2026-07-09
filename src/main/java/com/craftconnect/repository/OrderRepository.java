package com.craftconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craftconnect.entity.Order;
import com.craftconnect.entity.OrderStatus;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    List<Order> findByBuyerId(Long buyerId);
    
    long countByStatus(OrderStatus status);

    List<Order> findTop5ByOrderByCreatedAtDesc();
}