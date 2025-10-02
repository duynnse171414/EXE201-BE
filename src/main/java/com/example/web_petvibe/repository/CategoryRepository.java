package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false")
    List<Category> findAllActive();

    @Query("SELECT c FROM Category c WHERE c.id = ?1 AND c.isDeleted = false")
    Optional<Category> findByIdActive(Long id);

    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = ?1 AND c.isDeleted = false")
    boolean existsByName(String name);
}
