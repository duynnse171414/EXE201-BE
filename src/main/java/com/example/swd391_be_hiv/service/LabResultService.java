package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.LabResult;
import com.example.swd391_be_hiv.entity.MedicalRecord;
import com.example.swd391_be_hiv.entity.Doctor;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.reponse.LabResultResponse;
import com.example.swd391_be_hiv.model.request.LabResultRequest;
import com.example.swd391_be_hiv.repository.DoctorRepository;
import com.example.swd391_be_hiv.repository.LabResultRepository;
import com.example.swd391_be_hiv.repository.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabResultService {

    @Autowired
    LabResultRepository labResultRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    public LabResultResponse createLabResult(LabResultRequest requestDTO) {
        try {

            MedicalRecord medicalRecord = medicalRecordRepository.findById(requestDTO.getMedicalRecordId())
                    .orElseThrow(() -> new NotFoundException("Medical record not found"));


            Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor not found"));


            LabResult labResult = new LabResult();
            labResult.setMedicalRecord(medicalRecord);
            labResult.setDoctorId(requestDTO.getDoctorId());
            labResult.setResult(requestDTO.getResult());
            labResult.setCd4Count(requestDTO.getCd4Count());
            labResult.setTestDate(requestDTO.getTestDate());
            labResult.setNote(requestDTO.getNote());

            LabResult savedLabResult = labResultRepository.save(labResult);
            return convertToResponseDTO(savedLabResult);

        } catch (Exception e) {
            throw new DuplicateEntity("Error creating lab result: " + e.getMessage());
        }
    }

    public List<LabResultResponse> getAllLabResults() {
        return labResultRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public LabResultResponse updateLabResult(LabResultRequest requestDTO, long labResultId) {
        LabResult existingLabResult = labResultRepository.findLabResultByLabResultId(labResultId);
        if (existingLabResult == null) {
            throw new NotFoundException("Lab result not found");
        }


        MedicalRecord medicalRecord = medicalRecordRepository.findById(requestDTO.getMedicalRecordId())
                .orElseThrow(() -> new NotFoundException("Medical record not found"));


        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));


        existingLabResult.setMedicalRecord(medicalRecord);
        existingLabResult.setDoctorId(requestDTO.getDoctorId());
        existingLabResult.setResult(requestDTO.getResult());
        existingLabResult.setCd4Count(requestDTO.getCd4Count());
        existingLabResult.setTestDate(requestDTO.getTestDate());
        existingLabResult.setNote(requestDTO.getNote());

        LabResult updatedLabResult = labResultRepository.save(existingLabResult);
        return convertToResponseDTO(updatedLabResult);
    }

    public LabResultResponse deleteLabResult(long labResultId) {
        LabResult labResult = labResultRepository.findLabResultByLabResultId(labResultId);
        if (labResult == null) {
            throw new NotFoundException("Lab result not found");
        }

        LabResultResponse response = convertToResponseDTO(labResult);
        labResultRepository.delete(labResult);
        return response;
    }

    public List<LabResultResponse> getLabResultsByMedicalRecord(Long medicalRecordId) {
        return labResultRepository.findLabResultsByMedicalRecord_MedicalRecordIdOrderByTestDateDesc(medicalRecordId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LabResultResponse> getLabResultsByDoctor(Long doctorId) {
        return labResultRepository.findLabResultsByDoctorIdOrderByTestDateDesc(doctorId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LabResultResponse> getLabResultsByDateRange(LocalDate startDate, LocalDate endDate) {
        return labResultRepository.findLabResultsByTestDateBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private LabResultResponse convertToResponseDTO(LabResult labResult) {
        LabResultResponse dto = new LabResultResponse();
        dto.setLabResultId(labResult.getLabResultId());
        dto.setMedicalRecordId(labResult.getMedicalRecord().getMedicalRecordId());
        dto.setDoctorId(labResult.getDoctorId());
        dto.setResult(labResult.getResult());
        dto.setCd4Count(labResult.getCd4Count());
        dto.setTestDate(labResult.getTestDate());
        dto.setNote(labResult.getNote());


        if (labResult.getMedicalRecord().getCustomer() != null) {
            dto.setPatientName(labResult.getMedicalRecord().getCustomer().getAccount().getFullName());
        }


        Doctor doctor = doctorRepository.findById(labResult.getDoctorId()).orElse(null);
        if (doctor != null) {
            dto.setDoctorName(doctor.getName());
        }

        return dto;
    }
}