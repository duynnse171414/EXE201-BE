package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.MedicalRecord;
import com.example.swd391_be_hiv.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

}