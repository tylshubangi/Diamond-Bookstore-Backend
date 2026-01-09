package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.entity.Review;
import com.bookstore.onlinebookstore.repository.ReviewRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReviewService {

    private final ReviewRepository reviewRepository;

    public AdminReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void deleteReview(int reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
