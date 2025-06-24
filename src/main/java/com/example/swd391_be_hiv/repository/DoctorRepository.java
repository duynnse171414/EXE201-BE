package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByDeletedFalse();

    @Query("SELECT d FROM Doctor d WHERE d.deleted = false")
    List<Doctor> findAllActiveDoctors();

    Optional<Doctor> findByIdAndDeletedFalse(Long id);

    Optional<Doctor> findByCustomerIdAndDeletedFalse(Long customerId);

    List<Doctor> findBySpecializationAndDeletedFalse(String specialization);


}
