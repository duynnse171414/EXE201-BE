package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.MedicalRecord;
import com.example.swd391_be_hiv.entity.Doctor;
import com.example.swd391_be_hiv.entity.Customer;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.reponse.MedicalRecordResponse;
import com.example.swd391_be_hiv.model.request.MedicalRecordRequest;
import com.example.swd391_be_hiv.repository.MedicalRecordRepository;
import com.example.swd391_be_hiv.repository.DoctorRepository;
import com.example.swd391_be_hiv.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    @Autowired
    MedicalRecordRepository medicalRecordRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    CustomerRepository customerRepository;

    public MedicalRecordResponse createMedicalRecord(MedicalRecordRequest requestDTO) {
        try {
            // Validate doctor and customer exist
            Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                    .orElseThrow(() -> new NotFoundException("Doctor not found"));

            Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                    .orElseThrow(() -> new NotFoundException("Customer not found"));

            // Convert DTO to entity
            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setCustomer(customer);
            medicalRecord.setDoctor(doctor);
            medicalRecord.setCd4Count(requestDTO.getCd4Count());
            medicalRecord.setViralLoad(requestDTO.getViralLoad());
            medicalRecord.setTreatmentHistory(requestDTO.getTreatmentHistory());
            medicalRecord.setLastUpdated(LocalDateTime.now());

            MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
            return convertToResponseDTO(savedRecord);

        } catch (Exception e) {
            throw new DuplicateEntity("Error creating medical record: " + e.getMessage());
        }
    }

    public List<MedicalRecordResponse> getAllMedicalRecord() {
        return medicalRecordRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public MedicalRecordResponse updateMedicalRecord(MedicalRecordRequest requestDTO, long recordId) {
        MedicalRecord existingRecord = medicalRecordRepository.findMedicalRecordByMedicalRecordId(recordId);
        if (existingRecord == null) {
            throw new NotFoundException("Medical record not found");
        }

        // Validate doctor and customer if they are being changed
        Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                .orElseThrow(() -> new NotFoundException("Doctor not found"));

        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        // Update fields
        existingRecord.setCustomer(customer);
        existingRecord.setDoctor(doctor);
        existingRecord.setCd4Count(requestDTO.getCd4Count());
        existingRecord.setViralLoad(requestDTO.getViralLoad());
        existingRecord.setTreatmentHistory(requestDTO.getTreatmentHistory());
        existingRecord.setLastUpdated(LocalDateTime.now());

        MedicalRecord updatedRecord = medicalRecordRepository.save(existingRecord);
        return convertToResponseDTO(updatedRecord);
    }

    public MedicalRecordResponse deleteMedicalRecord(long recordId) {
        MedicalRecord medicalRecord = medicalRecordRepository.findMedicalRecordByMedicalRecordId(recordId);
        if (medicalRecord == null) {
            throw new NotFoundException("Medical record not found");
        }

        MedicalRecordResponse response = convertToResponseDTO(medicalRecord);
        medicalRecordRepository.delete(medicalRecord);
        return response;
    }

    public List<MedicalRecordResponse> getMedicalRecordsByCustomer(Long customerId) {
        return medicalRecordRepository.findMedicalRecordsByCustomer_Id(customerId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordResponse> getMedicalRecordsByDoctor(Long doctorId) {
        return medicalRecordRepository.findMedicalRecordsByDoctor_Id(doctorId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordResponse> getMedicalRecordsByCd4Range(Integer minCd4, Integer maxCd4) {
        return medicalRecordRepository.findMedicalRecordsByCd4CountBetween(minCd4, maxCd4)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<MedicalRecordResponse> getMedicalRecordsByViralLoadRange(Double minViralLoad, Double maxViralLoad) {
        return medicalRecordRepository.findMedicalRecordsByViralLoadBetween(minViralLoad, maxViralLoad)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private MedicalRecordResponse convertToResponseDTO(MedicalRecord medicalRecord) {
        MedicalRecordResponse dto = new MedicalRecordResponse();
        dto.setMedicalRecordId(medicalRecord.getMedicalRecordId());
        dto.setCustomerId(medicalRecord.getCustomer().getId());
        dto.setCustomerName(medicalRecord.getCustomer().getAccount().getFullName());
        dto.setDoctorId(medicalRecord.getDoctor().getId());
        dto.setDoctorName(medicalRecord.getDoctor().getName());
        dto.setCd4Count(medicalRecord.getCd4Count());
        dto.setViralLoad(medicalRecord.getViralLoad());
        dto.setTreatmentHistory(medicalRecord.getTreatmentHistory());
        dto.setLastUpdated(medicalRecord.getLastUpdated());

        return dto;
    }
}