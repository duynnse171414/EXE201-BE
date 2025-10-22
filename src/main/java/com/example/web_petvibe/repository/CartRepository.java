package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.isDeleted = false")
    List<Cart> findAllActive();

    @Query("SELECT c FROM Cart c WHERE c.id = ?1 AND c.isDeleted = false")
    Optional<Cart> findByIdActive(Long id);

    @Query("SELECT c FROM Cart c WHERE c.userId = ?1 AND c.isDeleted = false")
    List<Cart> findByUserIdActive(Long userId);

    @Query("SELECT COUNT(c) > 0 FROM Cart c WHERE c.userId = ?1 AND c.productId = ?2 AND c.isDeleted = false")
    boolean existsByUserIdAndProductId(Long userId, Long productId);
}