package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.TreatmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreatmentPlanRepository extends JpaRepository<TreatmentPlan, Long> {

    TreatmentPlan findTreatmentPlanByPlanId(Long planId);

    List<TreatmentPlan> findTreatmentPlansByMedicalRecord_MedicalRecordId(Long medicalRecordId);

    List<TreatmentPlan> findTreatmentPlansByDoctor_Id(Long doctorId);

    List<TreatmentPlan> findTreatmentPlansByArvRegimen(String arvRegimen);

    List<TreatmentPlan> findTreatmentPlansByApplicableGroup(String applicableGroup);

    // Get treatment plans by active doctors only (not deleted)
    List<TreatmentPlan> findTreatmentPlansByDoctor_DeletedFalse();
}