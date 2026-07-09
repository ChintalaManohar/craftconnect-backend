package com.craftconnect.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.craftconnect.dto.ReviewRequest;
import com.craftconnect.entity.Review;
import com.craftconnect.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(
            ReviewService reviewService) {

        this.reviewService = reviewService;
    }

    @PostMapping
    public String addReview(
            @RequestBody
            ReviewRequest request) {

        return reviewService
                .addReview(request);
    }

    @GetMapping("/{productId}")
    public List<Review> getReviews(
            @PathVariable Long productId) {

        return reviewService
                .getProductReviews(
                        productId);
    }

    @GetMapping("/{productId}/rating")
    public Double getAverageRating(
            @PathVariable Long productId) {

        return reviewService
                .getAverageRating(
                        productId);
    }
}