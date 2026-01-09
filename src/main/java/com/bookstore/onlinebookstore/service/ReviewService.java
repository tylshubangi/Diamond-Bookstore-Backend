package com.bookstore.onlinebookstore.service;

import com.bookstore.onlinebookstore.dto.*;
import com.bookstore.onlinebookstore.entity.*;
import com.bookstore.onlinebookstore.repository.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public ReviewService(
            ReviewRepository reviewRepository,
            BookRepository bookRepository,
            OrderRepository orderRepository,
            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    // 🔐 current user
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ⭐ ADD REVIEW
    public void addReview(ReviewRequestDTO request) {

        User user = getCurrentUser();
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // ✅ Check purchase
        boolean purchased = orderRepository
                .existsByUserAndItems_Book(user, book);

        if (!purchased) {
            throw new RuntimeException(
                    "You can review only purchased books");
        }

        // ❌ Prevent duplicate review
        if (reviewRepository.findByUserAndBook(user, book).isPresent()) {
            throw new RuntimeException("You already reviewed this book");
        }

        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    // 👀 GET REVIEWS BY BOOK
    public List<ReviewResponseDTO> getReviewsByBook(int bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return reviewRepository.findByBook(book)
                .stream()
                .map(review -> {
                    ReviewResponseDTO dto = new ReviewResponseDTO();
                    dto.setUserName(review.getUser().getName());
                    dto.setRating(review.getRating());
                    dto.setComment(review.getComment());
                    dto.setCreatedAt(review.getCreatedAt());
                    return dto;
                })
                .toList();
    }

    // ⭐ AVERAGE RATING
    public double getAverageRating(int bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        List<Review> reviews = reviewRepository.findByBook(book);

        if (reviews.isEmpty())
            return 0.0;

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
