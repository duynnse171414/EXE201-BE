package com.example.swd391_be_hiv.model.reponse;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MedicalRecordResponse {

    private Long medicalRecordId;

    private Long customerId;

    private String customerName;

    private Long doctorId;

    private String doctorName;

    private LocalDateTime lastUpdated;

    private Integer cd4Count;

    private Double viralLoad;

    private String treatmentHistory;

    // Additional fields for enhanced response
    private String cd4Status;

    private String viralLoadStatus;

    private Integer treatmentPlanCount;

    private Integer labResultCount;

    // Helper methods to determine status
    public String getCd4Status() {
        if (cd4Count == null) {
            return "Unknown";
        }
        if (cd4Count < 200) {
            return "Critical";
        } else if (cd4Count < 500) {
            return "Low";
        } else {
            return "Normal";
        }
    }

    public String getViralLoadStatus() {
        if (viralLoad == null) {
            return "Unknown";
        }
        if (viralLoad < 50) {
            return "Undetectable";
        } else if (viralLoad < 1000) {
            return "Low";
        } else if (viralLoad < 100000) {
            return "Moderate";
        } else {
            return "High";
        }
    }
}