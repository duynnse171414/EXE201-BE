package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.AiImageAnalysis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiImageAnalysisResponse {
    private String message;
    private Boolean success;
    private AiImageAnalysis data;
}