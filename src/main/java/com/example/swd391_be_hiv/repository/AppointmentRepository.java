package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Appointment;
import com.example.swd391_be_hiv.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCustomer_Id(Long customerId);

    List<Appointment> findByDeletedFalse();

    // Lấy appointments theo Doctor_ID và chưa bị xóa
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.deleted = false")
    List<Appointment> findByDoctorIdAndNotDeleted(@Param("doctorId") Long doctorId);

    // Lấy appointments theo Doctor_ID, status và chưa bị xóa
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = :status AND a.deleted = false")
    List<Appointment> findByDoctorIdAndStatusAndNotDeleted(@Param("doctorId") Long doctorId, @Param("status") String status);
}

