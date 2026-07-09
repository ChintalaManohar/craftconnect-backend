package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.AdminDashboardResponse;
import com.craftconnect.dto.AdminOrderResponse;
import com.craftconnect.dto.AdminProductResponse;
import com.craftconnect.dto.AdminUserResponse;
import com.craftconnect.entity.Order;
import com.craftconnect.entity.Product;
import com.craftconnect.entity.User;

public interface AdminService {

	List<AdminUserResponse> getAllUsers();
	
	List<AdminProductResponse> getAllProducts();
	List<AdminOrderResponse> getAllOrders();
    
    String deleteUser(Long userId);

    String deleteProduct(Long productId);
    
    AdminDashboardResponse getDashboard();
}