package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByCustomer_Id(Long customerId);
}

