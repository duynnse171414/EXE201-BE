package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    Staff findStaffByStaffId(Long staffId);

    Staff findStaffByEmail(String email);

    List<Staff> findStaffByIsDeletedFalse();

    List<Staff> findStaffByIsDeletedTrue();

    List<Staff> findStaffByGender(String gender);

    List<Staff> findStaffByNameContainingIgnoreCase(String name);

    List<Staff> findStaffByGenderAndIsDeletedFalse(String gender);

    @Query("SELECT DISTINCT s FROM Staff s JOIN s.educationContents ec WHERE ec IS NOT NULL")
    List<Staff> findStaffWithEducationContent();

    @Query("SELECT DISTINCT s FROM Staff s JOIN s.blogs b WHERE b IS NOT NULL")
    List<Staff> findStaffWithBlogs();

    @Query("SELECT s, COUNT(ec) FROM Staff s LEFT JOIN s.educationContents ec GROUP BY s")
    List<Object[]> countEducationContentByStaff();

    @Query("SELECT s, COUNT(b) FROM Staff s LEFT JOIN s.blogs b GROUP BY s")
    List<Object[]> countBlogsByStaff();

    @Query("SELECT s FROM Staff s WHERE s.phone = :phone AND s.isDeleted = false")
    Staff findActiveStaffByPhone(@Param("phone") String phone);

    @Query("SELECT s FROM Staff s LEFT JOIN s.educationContents ec " +
            "WHERE s.isDeleted = false " +
            "GROUP BY s ORDER BY COUNT(ec) DESC")
    List<Staff> findStaffOrderByEducationContentCount();

    @Query("SELECT s FROM Staff s LEFT JOIN s.blogs b " +
            "WHERE s.isDeleted = false " +
            "GROUP BY s ORDER BY COUNT(b) DESC")
    List<Staff> findStaffOrderByBlogCount();
}