package com.craftconnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.craftconnect.entity.OrderItem;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findByProductId(Long productId);
	
	List<OrderItem> findByProductArtisanId(
            Long artisanId);
	
	List<OrderItem> findByOrderId(Long orderId);
}