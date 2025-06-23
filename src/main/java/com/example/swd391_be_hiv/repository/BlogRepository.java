package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    Blog findBlogByBlogId(Long blogId);

    List<Blog> findBlogsByStaff_StaffId(Long staffId);

    List<Blog> findBlogsByTitleContainingIgnoreCase(String title);

    List<Blog> findBlogsByCreateDateAfter(LocalDateTime date);

    List<Blog> findBlogsByCreateDateBefore(LocalDateTime date);

    List<Blog> findBlogsByCreateDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Get blogs by active staff only (not deleted)
    List<Blog> findBlogsByStaff_IsDeletedFalse();

    // Custom query to get blogs with content containing specific text
    @Query("SELECT b FROM Blog b WHERE b.content LIKE %:keyword%")
    List<Blog> findBlogsByContentContaining(@Param("keyword") String keyword);

    // Custom query to get latest blogs (top 10 most recent)
    @Query("SELECT b FROM Blog b ORDER BY b.createDate DESC")
    List<Blog> findLatestBlogs();

    // Custom query to get blogs by title and content search
    @Query("SELECT b FROM Blog b WHERE b.title LIKE %:keyword% OR b.content LIKE %:keyword%")
    List<Blog> findBlogsByTitleOrContentContaining(@Param("keyword") String keyword);

    // Custom query to get blogs from specific month/year
    @Query("SELECT b FROM Blog b WHERE YEAR(b.createDate) = :year AND MONTH(b.createDate) = :month")
    List<Blog> findBlogsByYearAndMonth(@Param("year") int year, @Param("month") int month);

    // Custom query to get blogs count by staff
    @Query("SELECT COUNT(b) FROM Blog b WHERE b.staff.staffId = :staffId")
    Long countBlogsByStaffId(@Param("staffId") Long staffId);

    // Custom query to get blogs ordered by create date descending
    @Query("SELECT b FROM Blog b ORDER BY b.createDate DESC")
    List<Blog> findAllBlogsOrderByCreateDateDesc();
}