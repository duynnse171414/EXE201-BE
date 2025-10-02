package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductResponse {
    private String message;
    private boolean success;
    private Product product;
}
