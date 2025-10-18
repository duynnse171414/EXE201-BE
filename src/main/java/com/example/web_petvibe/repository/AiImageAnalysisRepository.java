package com.example.web_petvibe.repository;

import com.example.web_petvibe.entity.AiImageAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiImageAnalysisRepository extends JpaRepository<AiImageAnalysis, Long> {

    @Query("SELECT a FROM AiImageAnalysis a WHERE a.isDeleted = false")
    List<AiImageAnalysis> findAllActive();

    @Query("SELECT a FROM AiImageAnalysis a WHERE a.analysisId = ?1 AND a.isDeleted = false")
    Optional<AiImageAnalysis> findByIdActive(Long analysisId);

    @Query("SELECT a FROM AiImageAnalysis a WHERE a.userId = ?1 AND a.isDeleted = false ORDER BY a.analyzedAt DESC")
    List<AiImageAnalysis> findByUserIdActive(Long userId);

    @Query("SELECT a FROM AiImageAnalysis a WHERE a.petId = ?1 AND a.isDeleted = false ORDER BY a.analyzedAt DESC")
    List<AiImageAnalysis> findByPetIdActive(Long petId);

    @Query("SELECT COUNT(a) FROM AiImageAnalysis a WHERE a.userId = ?1 AND a.isDeleted = false")
    Long countByUserId(Long userId);
}