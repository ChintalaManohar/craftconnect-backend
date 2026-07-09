package com.craftconnect.service.impl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.craftconnect.dto.ReviewRequest;
import com.craftconnect.entity.*;
import com.craftconnect.repository.*;
import com.craftconnect.service.ReviewService;

@Service
public class ReviewServiceImpl
        implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public ReviewServiceImpl(
            ReviewRepository reviewRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            OrderItemRepository orderItemRepository) {

        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
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
    public String addReview(
            ReviewRequest request) {

        User buyer = getCurrentUser();

        Product product =
                productRepository.findById(
                        request.getProductId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product Not Found"));
        
        if (request.getRating() < 1
                || request.getRating() > 5) {

            throw new RuntimeException(
                    "Rating must be between 1 and 5");
        }

        boolean purchased =
                orderItemRepository
                        .findByProductId(
                                product.getId())
                        .stream()
                        .anyMatch(item ->
                                item.getOrder()
                                        .getBuyer()
                                        .getId()
                                        .equals(
                                                buyer.getId())
                                &&
                                item.getOrder()
                                        .getStatus()
                                        == OrderStatus.DELIVERED);

        if (!purchased) {

            throw new RuntimeException(
                    "You can review only delivered products");
        }

        if (reviewRepository
                .findByBuyerIdAndProductId(
                        buyer.getId(),
                        product.getId())
                .isPresent()) {

            throw new RuntimeException(
                    "Review already submitted");
        }

        Review review = new Review();

        review.setBuyer(buyer);
        review.setProduct(product);
        review.setRating(
                request.getRating());
        review.setComment(
                request.getComment());

        reviewRepository.save(review);

        return "Review Added Successfully";
    }

    @Override
    public List<Review> getProductReviews(
            Long productId) {

        return reviewRepository
                .findByProductId(productId);
    }

    @Override
    public Double getAverageRating(
            Long productId) {

        List<Review> reviews =
                reviewRepository
                        .findByProductId(productId);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        return reviews.stream()
                .mapToInt(
                        Review::getRating)
                .average()
                .orElse(0.0);
    }
}