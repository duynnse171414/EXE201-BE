package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.model.reponse.MedicalRecordResponse;
import com.example.swd391_be_hiv.model.request.MedicalRecordRequest;
import com.example.swd391_be_hiv.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/medical-record")
public class MedicalRecordAPI {

    @Autowired
    MedicalRecordService medicalRecordService;

    @PostMapping
    public ResponseEntity<MedicalRecordResponse> createMedicalRecord(@Valid @RequestBody MedicalRecordRequest requestDTO) {
        MedicalRecordResponse response = medicalRecordService.createMedicalRecord(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getMedicalRecord")
    public ResponseEntity<List<MedicalRecordResponse>> getMedicalRecord() {
        List<MedicalRecordResponse> medicalRecordList = medicalRecordService.getAllMedicalRecord();
        return ResponseEntity.ok(medicalRecordList);
    }

    @PutMapping("{id}")
    public ResponseEntity<MedicalRecordResponse> updateMedicalRecord(@Valid @RequestBody MedicalRecordRequest requestDTO, @PathVariable long id) {
        MedicalRecordResponse response = medicalRecordService.updateMedicalRecord(requestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<MedicalRecordResponse> deleteMedicalRecord(@PathVariable long id) {
        MedicalRecordResponse response = medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<MedicalRecordResponse>> getMedicalRecordsByCustomer(@PathVariable Long customerId) {
        List<MedicalRecordResponse> medicalRecords = medicalRecordService.getMedicalRecordsByCustomer(customerId);
        return ResponseEntity.ok(medicalRecords);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalRecordResponse>> getMedicalRecordsByDoctor(@PathVariable Long doctorId) {
        List<MedicalRecordResponse> medicalRecords = medicalRecordService.getMedicalRecordsByDoctor(doctorId);
        return ResponseEntity.ok(medicalRecords);
    }

    @GetMapping("/cd4-range")
    public ResponseEntity<List<MedicalRecordResponse>> getMedicalRecordsByCd4Range(
            @RequestParam Integer minCd4,
            @RequestParam Integer maxCd4) {
        List<MedicalRecordResponse> medicalRecords = medicalRecordService.getMedicalRecordsByCd4Range(minCd4, maxCd4);
        return ResponseEntity.ok(medicalRecords);
    }

    @GetMapping("/viral-load-range")
    public ResponseEntity<List<MedicalRecordResponse>> getMedicalRecordsByViralLoadRange(
            @RequestParam Double minViralLoad,
            @RequestParam Double maxViralLoad) {
        List<MedicalRecordResponse> medicalRecords = medicalRecordService.getMedicalRecordsByViralLoadRange(minViralLoad, maxViralLoad);
        return ResponseEntity.ok(medicalRecords);
    }
}