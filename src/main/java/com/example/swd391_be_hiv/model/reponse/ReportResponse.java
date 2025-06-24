package com.example.swd391_be_hiv.model.reponse;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReportResponse {
    private Long reportId;
    private String generatedBy;
    private String reportType;
    private String content;
    private LocalDateTime generatedAt;
}