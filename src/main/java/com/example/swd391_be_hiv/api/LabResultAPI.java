package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.model.reponse.LabResultResponse;
import com.example.swd391_be_hiv.model.request.LabResultRequest;
import com.example.swd391_be_hiv.service.LabResultService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/lab-result")
public class LabResultAPI {

    @Autowired
    LabResultService labResultService;

    @PostMapping
    public ResponseEntity<LabResultResponse> createLabResult(@Valid @RequestBody LabResultRequest requestDTO) {
        LabResultResponse response = labResultService.createLabResult(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getLabResults")
    public ResponseEntity<List<LabResultResponse>> getLabResults() {
        List<LabResultResponse> labResultList = labResultService.getAllLabResults();
        return ResponseEntity.ok(labResultList);
    }

    @PutMapping("{id}")
    public ResponseEntity<LabResultResponse> updateLabResult(@Valid @RequestBody LabResultRequest requestDTO, @PathVariable long id) {
        LabResultResponse response = labResultService.updateLabResult(requestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<LabResultResponse> deleteLabResult(@PathVariable long id) {
        LabResultResponse response = labResultService.deleteLabResult(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/medical-record/{medicalRecordId}")
    public ResponseEntity<List<LabResultResponse>> getLabResultsByMedicalRecord(@PathVariable Long medicalRecordId) {
        List<LabResultResponse> labResults = labResultService.getLabResultsByMedicalRecord(medicalRecordId);
        return ResponseEntity.ok(labResults);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<LabResultResponse>> getLabResultsByDoctor(@PathVariable Long doctorId) {
        List<LabResultResponse> labResults = labResultService.getLabResultsByDoctor(doctorId);
        return ResponseEntity.ok(labResults);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<LabResultResponse>> getLabResultsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<LabResultResponse> labResults = labResultService.getLabResultsByDateRange(startDate, endDate);
        return ResponseEntity.ok(labResults);
    }
}