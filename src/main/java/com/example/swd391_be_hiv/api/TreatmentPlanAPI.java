

package com.example.swd391_be_hiv.api;
import com.example.swd391_be_hiv.model.reponse.TreatmentPlanResponse;
import com.example.swd391_be_hiv.model.request.TreatmentPlanRequest;
import com.example.swd391_be_hiv.service.TreatmentPlanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/treatment-plan")
public class TreatmentPlanAPI {

    @Autowired
    TreatmentPlanService treatmentPlanService;

    @PostMapping
    public ResponseEntity<TreatmentPlanResponse> createTreatmentPlan(@Valid @RequestBody TreatmentPlanRequest requestDTO) {
        TreatmentPlanResponse response = treatmentPlanService.createTreatmentPlan(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getTreatmentPlan")
    public ResponseEntity<List<TreatmentPlanResponse>> getTreatmentPlan() {
        List<TreatmentPlanResponse> treatmentPlanList = treatmentPlanService.getAllTreatmentPlan();
        return ResponseEntity.ok(treatmentPlanList);
    }

    @PutMapping("{id}")
    public ResponseEntity<TreatmentPlanResponse> updateTreatmentPlan(@Valid @RequestBody TreatmentPlanRequest requestDTO, @PathVariable long id) {
        TreatmentPlanResponse response = treatmentPlanService.updateTreatmentPlan(requestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TreatmentPlanResponse> deleteTreatmentPlan(@PathVariable long id) {
        TreatmentPlanResponse response = treatmentPlanService.deleteTreatmentPlan(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/medical-record/{medicalRecordId}")
    public ResponseEntity<List<TreatmentPlanResponse>> getTreatmentPlansByMedicalRecord(@PathVariable Long medicalRecordId) {
        List<TreatmentPlanResponse> treatmentPlans = treatmentPlanService.getTreatmentPlansByMedicalRecord(medicalRecordId);
        return ResponseEntity.ok(treatmentPlans);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<TreatmentPlanResponse>> getTreatmentPlansByDoctor(@PathVariable Long doctorId) {
        List<TreatmentPlanResponse> treatmentPlans = treatmentPlanService.getTreatmentPlansByDoctor(doctorId);
        return ResponseEntity.ok(treatmentPlans);
    }
}