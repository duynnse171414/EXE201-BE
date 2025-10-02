package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private String message;
    private boolean success;
    private Category category;
}