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

    // Get education contents by active staff only (not deleted)
    List<EducationContent> findEducationContentsByStaff_IsDeletedFalse();

    // Custom query to get education contents with content containing specific text
    @Query("SELECT ec FROM EducationContent ec WHERE ec.content LIKE %:keyword%")
    List<EducationContent> findEducationContentsByContentContaining(@Param("keyword") String keyword);

    // Custom query to get latest education contents (top 10 most recent)
    @Query("SELECT ec FROM EducationContent ec ORDER BY ec.createdAt DESC")
    List<EducationContent> findLatestEducationContents();

    // Custom query to get education contents by title and content search
    @Query("SELECT ec FROM EducationContent ec WHERE ec.title LIKE %:keyword% OR ec.content LIKE %:keyword%")
    List<EducationContent> findEducationContentsByTitleOrContentContaining(@Param("keyword") String keyword);

    // Custom query to get education contents from specific month/year
    @Query("SELECT ec FROM EducationContent ec WHERE YEAR(ec.createdAt) = :year AND MONTH(ec.createdAt) = :month")
    List<EducationContent> findEducationContentsByYearAndMonth(@Param("year") int year, @Param("month") int month);

    // Custom query to get education contents count by staff
    @Query("SELECT COUNT(ec) FROM EducationContent ec WHERE ec.staff.staffId = :staffId")
    Long countEducationContentsByStaffId(@Param("staffId") Long staffId);

    // Custom query to get education contents ordered by created date descending
    @Query("SELECT ec FROM EducationContent ec ORDER BY ec.createdAt DESC")
    List<EducationContent> findAllEducationContentsOrderByCreatedAtDesc();

    // Custom query to get popular education contents (this would need view/like tracking)
    @Query("SELECT ec FROM EducationContent ec ORDER BY ec.createdAt DESC")
    List<EducationContent> findPopularEducationContents();
}