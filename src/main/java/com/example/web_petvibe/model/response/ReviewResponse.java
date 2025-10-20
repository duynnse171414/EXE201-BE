package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private String message;
    private boolean success;
    private Review data;
}