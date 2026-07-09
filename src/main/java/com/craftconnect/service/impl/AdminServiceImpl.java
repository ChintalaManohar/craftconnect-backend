package com.craftconnect.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.craftconnect.dto.AdminDashboardResponse;
import com.craftconnect.dto.AdminOrderItemResponse;
import com.craftconnect.dto.AdminOrderResponse;
import com.craftconnect.dto.AdminProductResponse;
import com.craftconnect.dto.AdminRecentArtisanResponse;
import com.craftconnect.dto.AdminRecentOrderResponse;
import com.craftconnect.dto.AdminRecentProductResponse;
import com.craftconnect.dto.AdminUserResponse;
import com.craftconnect.entity.Order;
import com.craftconnect.entity.OrderItem;
import com.craftconnect.entity.OrderStatus;
import com.craftconnect.entity.Payment;
import com.craftconnect.entity.PaymentMethod;
import com.craftconnect.entity.Product;
import com.craftconnect.entity.Role;
import com.craftconnect.entity.User;
import com.craftconnect.repository.OrderItemRepository;
import com.craftconnect.repository.OrderRepository;
import com.craftconnect.repository.PaymentRepository;
import com.craftconnect.repository.ProductRepository;
import com.craftconnect.repository.UserRepository;
import com.craftconnect.service.AdminService;

@Service
public class AdminServiceImpl
        implements AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
  

    public AdminServiceImpl(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            PaymentRepository paymentRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository =orderItemRepository;
        this.paymentRepository =paymentRepository;
    }
    @Override
    public List<AdminUserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> new AdminUserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole(),

                        user.getVillage(),
                        user.getDistrict(),
                        user.getState(),
                        user.getPincode(),
                        user.getCategory(),

                        user.getAvatar(),
                        user.getCoverImage(),

                        user.getStoryTitle(),
                        user.getStoryDescription()
                        ,user.isActive()
                ))
                .toList();
    }

    @Override
    public List<AdminProductResponse> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(product -> new AdminProductResponse(

                        product.getId(),

                        product.getName(),

                        product.getDescription(),

                        product.getPrice(),

                        product.getQuantity(),

                        product.getActive(),

                        product.getImageUrl(),

                        product.getCreatedAt(),

                        product.getCategory().getId(),

                        product.getCategory().getName(),

                        product.getArtisan().getId(),

                        product.getArtisan().getFullName()

                ))
                .toList();
    }

    @Override
    public List<AdminOrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(order -> {

                    List<AdminOrderItemResponse> items =
                            orderItemRepository
                                    .findByOrderId(order.getId())
                                    .stream()
                                    .map(item ->
                                            new AdminOrderItemResponse(
                                                    item.getProduct().getId(),
                                                    item.getProduct().getName(),
                                                    item.getProduct().getImageUrl(),
                                                    item.getQuantity(),
                                                    item.getPrice(),
                                                    item.getProduct()
                                                            .getArtisan()
                                                            .getId(),
                                                    item.getProduct()
                                                            .getArtisan()
                                                            .getFullName()
                                            ))
                                    .toList();

                    Payment payment =
                            paymentRepository
                                    .findByOrderId(order.getId())
                                    .orElse(null);

                    return new AdminOrderResponse(
                            order.getId(),

                            order.getBuyer().getId(),

                            order.getBuyer().getFullName(),

                            order.getBuyer().getEmail(),

                            order.getTotalAmount(),

                            order.getStatus(),

                            order.getCreatedAt(),

                            order.getPaymentMethod().name(),

                            order.getPaymentMethod() == PaymentMethod.COD
                                    ? "PENDING"
                                    : payment == null
                                        ? "PENDING"
                                        : payment.getStatus().name(),

                            payment == null
                                    ? null
                                    : payment.getRazorpayPaymentId(),
                            items
                    );
                })
                .toList();
    }
    @Override
    public String deleteUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        if (user.getRole() == Role.ADMIN) {
            throw new RuntimeException("Admin cannot be deactivated");
        }

        if (!user.isActive()) {
            throw new RuntimeException("User already deactivated");
        }

        user.setActive(false);

        userRepository.save(user);

        return "User Deactivated Successfully";
    }
    @Override
    public String deleteProduct(Long productId) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product Not Found"));

        product.setActive(false);

        productRepository.save(product);

        return "Product Deleted Successfully";
    }
    @Override
    public AdminDashboardResponse getDashboard() {

        Long totalCustomers =
                userRepository.countByRole(Role.BUYER);

        Long totalArtisans =
                userRepository.countByRole(Role.ARTISAN);

        Long totalProducts =
                productRepository.countByActiveTrue();

        Long totalOrders =
                orderRepository.count();

        BigDecimal totalRevenue =
                paymentRepository.getTotalRevenue();

        Long pendingOrders =
                orderRepository.countByStatus(
                        OrderStatus.PENDING_PAYMENT);

        List<AdminRecentOrderResponse> recentOrders =
                orderRepository
                        .findTop5ByOrderByCreatedAtDesc()
                        .stream()
                        .map(order -> {

                            List<OrderItem> items =
                                    orderItemRepository.findByOrderId(order.getId());

                            OrderItem item =
                                    items.isEmpty() ? null : items.get(0);

                            return new AdminRecentOrderResponse(
                                    order.getId(),
                                    order.getBuyer().getFullName(),
                                    item != null
                                            ? item.getProduct().getName()
                                            : "N/A",
                                    item != null
                                            ? item.getQuantity()
                                            : 0,
                                    item != null
                                            ? item.getPrice()
                                            : BigDecimal.ZERO,
                                    order.getStatus().name()
                            );
                        })
                        .toList();
        List<AdminRecentProductResponse> recentProducts =
                productRepository
                        .findTop5ByActiveTrueOrderByCreatedAtDesc()
                        .stream()
                        .map(product ->
                                new AdminRecentProductResponse(
                                        product.getId(),
                                        product.getName(),
                                        product.getArtisan().getFullName(),
                                        product.getPrice(),
                                        product.getImageUrl()
                                ))
                        .toList();

        List<AdminRecentArtisanResponse> recentArtisans =
                userRepository
                        .findTop5ByRoleOrderByIdDesc(Role.ARTISAN)
                        .stream()
                        .map(user ->
                                new AdminRecentArtisanResponse(
                                        user.getId(),
                                        user.getFullName(),
                                        user.getCategory(),
                                        user.getVillage(),
                                        user.getAvatar(),
                                        user.getStoryTitle() != null
                                                && !user.getStoryTitle().isBlank()
                                ))
                        .toList();

        return new AdminDashboardResponse(
                totalCustomers,
                totalArtisans,
                totalProducts,
                totalOrders,
                totalRevenue,
                pendingOrders,
                recentOrders,
                recentProducts,
                recentArtisans
        );
    }
}