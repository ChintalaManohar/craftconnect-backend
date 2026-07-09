package com.craftconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.AdminDashboardResponse;
import com.craftconnect.dto.AdminOrderResponse;
import com.craftconnect.dto.AdminProductResponse;
import com.craftconnect.dto.AdminUserResponse;
import com.craftconnect.entity.Order;
import com.craftconnect.entity.Product;
import com.craftconnect.entity.User;
import com.craftconnect.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(
            AdminService adminService) {

        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<AdminUserResponse> getAllUsers() {

        return adminService.getAllUsers();
    }

    @GetMapping("/products")
    public List<AdminProductResponse> getAllProducts() {

        return adminService.getAllProducts();
    }

    @GetMapping("/orders")
    public List<AdminOrderResponse> getAllOrders() {

        return adminService.getAllOrders();
    }
    @DeleteMapping("/users/{userId}")
    public String deleteUser(
            @PathVariable Long userId) {

        return adminService
                .deleteUser(userId);
    }
    @DeleteMapping("/products/{productId}")
    public String deleteProduct(
            @PathVariable Long productId) {

        return adminService
                .deleteProduct(productId);
    }
    @GetMapping("/dashboard")
    public AdminDashboardResponse getDashboard() {

        return adminService.getDashboard();
    }
}