package com.example.web_petvibe.service;

import com.example.web_petvibe.entity.Review;
import com.example.web_petvibe.repository.ReviewRepository;
import com.example.web_petvibe.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private final OrderRepository orderRepository;

    // Lấy tất cả review
    public List<Review> getAllReviews() {
        return reviewRepository.findAllActive();
    }

    // Lấy review theo ID
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findByIdActive(id);
    }

    // Lấy review theo product ID
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductIdActive(productId);
    }

    // Lấy review theo user ID
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserIdActive(userId);
    }

    // Tạo mới review
    public Review createReview(Review review) {
        // Kiểm tra xem account đã mua sản phẩm và đơn hàng đã hoàn thành chưa
        // Option 1: Chỉ cho phép review khi DELIVERED (Khuyên dùng)
        boolean hasPurchased = orderRepository.existsByAccountIdAndProductIdAndOrderCompleted(
                review.getUserId(),
                review.getProductId()
        );

        // Option 2: Cho phép review khi SHIPPED hoặc DELIVERED (Không khuyên)
        // boolean hasPurchased = orderRepository.existsByAccountIdAndProductIdAndOrderShippedOrDelivered(
        //     review.getUserId(),
        //     review.getProductId()
        // );

        if (!hasPurchased) {
            throw new RuntimeException("You can only review products you have successfully purchased and received");
        }

        // Kiểm tra xem user đã review sản phẩm này chưa
        if (reviewRepository.existsByUserIdAndProductId(review.getUserId(), review.getProductId())) {
            throw new RuntimeException("User has already reviewed this product");
        }

        // Validate rating (1-5)
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        review.setDeleted(false);
        review.setCreatedAt(LocalDateTime.now());
        review.setIsVerifiedPurchase(true); // Tự động set verified vì đã kiểm tra đơn hàng
        return reviewRepository.save(review);
    }

    // Cập nhật review
    public Review updateReview(Long id, Review reviewUpdate) {
        Optional<Review> existingReview = reviewRepository.findByIdActive(id);

        if (existingReview.isEmpty()) {
            throw new RuntimeException("Review not found with id: " + id);
        }

        Review review = existingReview.get();

        // Validate rating (1-5)
        if (reviewUpdate.getRating() < 1 || reviewUpdate.getRating() > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Cập nhật các field
        if (reviewUpdate.getRating() != 0) {
            review.setRating(reviewUpdate.getRating());
        }
        if (reviewUpdate.getComment() != null) {
            review.setComment(reviewUpdate.getComment());
        }
        if (reviewUpdate.getIsVerifiedPurchase() != null) {
            review.setIsVerifiedPurchase(reviewUpdate.getIsVerifiedPurchase());
        }

        return reviewRepository.save(review);
    }

    // Xóa mềm review
    public void deleteReview(Long id) {
        Optional<Review> review = reviewRepository.findByIdActive(id);
        if (review.isPresent()) {
            Review r = review.get();
            r.setDeleted(true);
            reviewRepository.save(r);
        } else {
            throw new RuntimeException("Review not found with id: " + id);
        }
    }
}