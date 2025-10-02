package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductResponse {
    private String message;
    private boolean success;
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private Category category;
}
