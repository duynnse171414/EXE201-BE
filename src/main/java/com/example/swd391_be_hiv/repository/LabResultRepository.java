package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.LabResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LabResultRepository extends JpaRepository<LabResult, Long> {

    LabResult findLabResultByLabResultId(Long labResultId);

    List<LabResult> findLabResultsByMedicalRecord_MedicalRecordId(Long medicalRecordId);

    List<LabResult> findLabResultsByDoctorId(Long doctorId);

    List<LabResult> findLabResultsByTestDate(LocalDate testDate);

    List<LabResult> findLabResultsByTestDateBetween(LocalDate startDate, LocalDate endDate);

    List<LabResult> findLabResultsByMedicalRecord_MedicalRecordIdOrderByTestDateDesc(Long medicalRecordId);

    List<LabResult> findLabResultsByDoctorIdOrderByTestDateDesc(Long doctorId);
}