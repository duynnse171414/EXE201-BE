package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByDeletedFalse();

    // Hoặc sử dụng query tùy chỉnh
    @Query("SELECT d FROM Doctor d WHERE d.deleted = false")
    List<Doctor> findAllActiveDoctors();

    // Method này không cần thiết vì JpaRepository đã có findById
    // Nhưng nếu muốn giữ thì sửa như sau:
    Optional<Doctor> findByIdAndDeletedFalse(Long id);

    // Tìm theo customerId
    Optional<Doctor> findByCustomerIdAndDeletedFalse(Long customerId);

    // Tìm theo specialization
    List<Doctor> findBySpecializationAndDeletedFalse(String specialization);


}
