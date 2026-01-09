package com.bookstore.onlinebookstore.controller;

import com.bookstore.onlinebookstore.dto.*;
import com.bookstore.onlinebookstore.service.ReviewService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // ⭐ Submit review
    @PostMapping
    public void submitReview(@RequestBody ReviewRequestDTO request) {
        reviewService.addReview(request);
    }

    // 👀 View reviews by book
    @GetMapping("/book/{bookId}")
    public List<ReviewResponseDTO> getReviews(@PathVariable int bookId) {
        return reviewService.getReviewsByBook(bookId);
    }

    // ⭐ Average rating
    @GetMapping("/book/{bookId}/average")
    public double getAverageRating(@PathVariable int bookId) {
        return reviewService.getAverageRating(bookId);
    }
}
