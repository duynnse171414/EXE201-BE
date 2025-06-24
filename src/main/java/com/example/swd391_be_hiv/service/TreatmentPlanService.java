package com.example.swd391_be_hiv.service;
import com.example.swd391_be_hiv.entity.TreatmentPlan;
import com.example.swd391_be_hiv.entity.Doctor;
import com.example.swd391_be_hiv.entity.MedicalRecord;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.reponse.TreatmentPlanResponse;
import com.example.swd391_be_hiv.model.request.TreatmentPlanRequest;
import com.example.swd391_be_hiv.repository.TreatmentPlanRepository;
import com.example.swd391_be_hiv.repository.DoctorRepository;
import com.example.swd391_be_hiv.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TreatmentPlanService {

    @Autowired
    TreatmentPlanRepository treatmentPlanRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    public TreatmentPlanResponse createTreatmentPlan(TreatmentPlanRequest requestDTO) {
        try {

            Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor not found"));

            MedicalRecord medicalRecord = medicalRecordRepository.findById(requestDTO.getMedicalRecordId())
                    .orElseThrow(() -> new NotFoundException("Medical record not found"));


            TreatmentPlan treatmentPlan = new TreatmentPlan();
            treatmentPlan.setDoctor(doctor);
            treatmentPlan.setMedicalRecord(medicalRecord);
            treatmentPlan.setArvRegimen(requestDTO.getArvRegimen());
            treatmentPlan.setApplicableGroup(requestDTO.getApplicableGroup());
            treatmentPlan.setNote(requestDTO.getNote());
            treatmentPlan.setStartDate(requestDTO.getStartDate());

            TreatmentPlan savedPlan = treatmentPlanRepository.save(treatmentPlan);
            return convertToResponseDTO(savedPlan);

        } catch (Exception e) {
            throw new DuplicateEntity("Error creating treatment plan: " + e.getMessage());
        }
    }

    public List<TreatmentPlanResponse> getAllTreatmentPlan() {
        return treatmentPlanRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public TreatmentPlanResponse updateTreatmentPlan(TreatmentPlanRequest requestDTO, long planId) {
        TreatmentPlan existingPlan = treatmentPlanRepository.findTreatmentPlanByPlanId(planId);
        if (existingPlan == null) {
            throw new NotFoundException("Treatment plan not found");
        }


        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        MedicalRecord medicalRecord = medicalRecordRepository.findById(requestDTO.getMedicalRecordId())
                .orElseThrow(() -> new NotFoundException("Medical record not found"));


        existingPlan.setDoctor(doctor);
        existingPlan.setMedicalRecord(medicalRecord);
        existingPlan.setArvRegimen(requestDTO.getArvRegimen());
        existingPlan.setApplicableGroup(requestDTO.getApplicableGroup());
        existingPlan.setNote(requestDTO.getNote());
        existingPlan.setStartDate(requestDTO.getStartDate());

        TreatmentPlan updatedPlan = treatmentPlanRepository.save(existingPlan);
        return convertToResponseDTO(updatedPlan);
    }

    public TreatmentPlanResponse deleteTreatmentPlan(long planId) {
        TreatmentPlan treatmentPlan = treatmentPlanRepository.findTreatmentPlanByPlanId(planId);
        if (treatmentPlan == null) {
            throw new NotFoundException("Treatment plan not found");
        }

        TreatmentPlanResponse response = convertToResponseDTO(treatmentPlan);
        treatmentPlanRepository.delete(treatmentPlan);
        return response;
    }

    public List<TreatmentPlanResponse> getTreatmentPlansByMedicalRecord(Long medicalRecordId) {
        return treatmentPlanRepository.findTreatmentPlansByMedicalRecord_MedicalRecordId(medicalRecordId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TreatmentPlanResponse> getTreatmentPlansByDoctor(Long doctorId) {
        return treatmentPlanRepository.findTreatmentPlansByDoctor_Id(doctorId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private TreatmentPlanResponse convertToResponseDTO(TreatmentPlan treatmentPlan) {
        TreatmentPlanResponse dto = new TreatmentPlanResponse();
        dto.setPlanId(treatmentPlan.getPlanId());
        dto.setMedicalRecordId(treatmentPlan.getMedicalRecord().getMedicalRecordId());
        dto.setDoctorId(treatmentPlan.getDoctor().getId());
        dto.setDoctorName(treatmentPlan.getDoctor().getName());
        dto.setArvRegimen(treatmentPlan.getArvRegimen());
        dto.setApplicableGroup(treatmentPlan.getApplicableGroup());
        dto.setNote(treatmentPlan.getNote());
        dto.setStartDate(treatmentPlan.getStartDate());


        if (treatmentPlan.getMedicalRecord().getCustomer() != null) {
            dto.setPatientName(treatmentPlan.getMedicalRecord().getCustomer().getAccount().getFullName()); // Assuming Customer has getName()
        }

        return dto;
    }
}