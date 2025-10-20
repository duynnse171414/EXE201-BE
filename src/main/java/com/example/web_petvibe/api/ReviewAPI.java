package com.example.web_petvibe.api;

import com.example.web_petvibe.entity.Review;
import com.example.web_petvibe.model.response.ApiResponse;
import com.example.web_petvibe.model.response.ReviewResponse;
import com.example.web_petvibe.service.ReviewService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@SecurityRequirement(name = "api")
public class ReviewAPI {

    @Autowired
    private ReviewService reviewService;

    // GET all reviews - Public hoặc Admin
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Review>> getAllReviews() {
        try {
            return ResponseEntity.ok(reviewService.getAllReviews());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET review by id - Public (ai cũng có thể xem)
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.isPresent()
                ? ResponseEntity.ok(new ReviewResponse("Review found successfully", true, review.get()))
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Review not found with id: " + id, false));
    }

    // GET reviews by product id - Public (ai cũng có thể xem review của sản phẩm)
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        try {
            return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // GET reviews by user id - Public hoặc có thể giới hạn
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // POST create review - User đã đăng nhập
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        try {
            Review created = reviewService.createReview(review);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewResponse("Review created successfully", true, created));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));
        }
    }

    // PUT update review - Chỉ owner hoặc admin
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @reviewService.isReviewOwner(#id)")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody Review review) {
        try {
            Review updated = reviewService.updateReview(id, review);
            return ResponseEntity.ok(new ReviewResponse("Review updated successfully", true, updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }

    // DELETE review - Chỉ owner hoặc admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @reviewService.isReviewOwner(#id)")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(new ApiResponse("Review deleted successfully", true));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), false));
        }
    }
}