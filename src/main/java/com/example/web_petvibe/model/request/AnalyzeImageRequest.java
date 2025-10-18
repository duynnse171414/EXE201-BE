package com.example.web_petvibe.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyzeImageRequest {
    private Long userId;
    private Long petId;
    private String imageUrl;
}