package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.entity.Review;
import com.bookstore.onlinebookstore.service.AdminReviewService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    public AdminReviewController(AdminReviewService adminReviewService) {
        this.adminReviewService = adminReviewService;
    }

    @GetMapping
    public List<Review> getAllReviews() {
        return adminReviewService.getAllReviews();
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable int reviewId) {
        adminReviewService.deleteReview(reviewId);
    }
}
