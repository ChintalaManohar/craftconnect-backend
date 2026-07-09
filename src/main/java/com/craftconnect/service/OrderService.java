package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.OrderResponse;
import com.craftconnect.dto.PlaceOrderRequest;
import com.craftconnect.entity.OrderStatus;

public interface OrderService {

	OrderResponse placeOrder(PlaceOrderRequest request);
	
	String markAsDelivered(Long orderId);
	
	String cancelOrder(Long orderId);

	List<OrderResponse> getMyOrders();
}