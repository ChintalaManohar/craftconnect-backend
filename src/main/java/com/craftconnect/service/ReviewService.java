package com.craftconnect.service;

import java.util.List;

import com.craftconnect.dto.ReviewRequest;
import com.craftconnect.entity.Review;

public interface ReviewService {

    String addReview(ReviewRequest request);

    List<Review> getProductReviews(Long productId);

    Double getAverageRating(Long productId);
}