package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.isDeleted = false")
    List<Review> findAllActive();

    @Query("SELECT r FROM Review r WHERE r.id = ?1 AND r.isDeleted = false")
    Optional<Review> findByIdActive(Long id);

    @Query("SELECT r FROM Review r WHERE r.productId = ?1 AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<Review> findByProductIdActive(Long productId);

    @Query("SELECT r FROM Review r WHERE r.userId = ?1 AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<Review> findByUserIdActive(Long userId);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.userId = ?1 AND r.productId = ?2 AND r.isDeleted = false")
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}