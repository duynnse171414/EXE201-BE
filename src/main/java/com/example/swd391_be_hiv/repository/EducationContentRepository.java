package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.EducationContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EducationContentRepository extends JpaRepository<EducationContent, Long> {

    EducationContent findEducationContentByPostId(Long postId);

    List<EducationContent> findEducationContentsByStaff_StaffId(Long staffId);

    List<EducationContent> findEducationContentsByTitleContainingIgnoreCase(String title);

    List<EducationContent> findEducationContentsByCreatedAtAfter(LocalDateTime date);

    List<EducationContent> findEducationContentsByCreatedAtBefore(LocalDateTime date);

    List<EducationContent> findEducationContentsByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<EducationContent> findEducationContentsByStaff_IsDeletedFalse();

    @Query("SELECT ec FROM EducationContent ec WHERE ec.content LIKE %:keyword%")
    List<EducationContent> findEducationContentsByContentContaining(@Param("keyword") String keyword);

    @Query("SELECT ec FROM EducationContent ec ORDER BY ec.createdAt DESC")
    List<EducationContent> findLatestEducationContents();

    @Query("SELECT ec FROM EducationContent ec WHERE ec.title LIKE %:keyword% OR ec.content LIKE %:keyword%")
    List<EducationContent> findEducationContentsByTitleOrContentContaining(@Param("keyword") String keyword);

    @Query("SELECT ec FROM EducationContent ec WHERE YEAR(ec.createdAt) = :year AND MONTH(ec.createdAt) = :month")
    List<EducationContent> findEducationContentsByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Query("SELECT COUNT(ec) FROM EducationContent ec WHERE ec.staff.staffId = :staffId")
    Long countEducationContentsByStaffId(@Param("staffId") Long staffId);

    @Query("SELECT ec FROM EducationContent ec ORDER BY ec.createdAt DESC")
    List<EducationContent> findAllEducationContentsOrderByCreatedAtDesc();

    @Query("SELECT ec FROM EducationContent ec ORDER BY ec.createdAt DESC")
    List<EducationContent> findPopularEducationContents();
}