package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    List<Product> findAllActive();

    @Query("SELECT p FROM Product p WHERE p.id = ?1 AND p.isDeleted = false")
    Optional<Product> findByIdActive(Long id);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 AND p.isDeleted = false")
    List<Product> findByCategoryId(Long categoryId);
}