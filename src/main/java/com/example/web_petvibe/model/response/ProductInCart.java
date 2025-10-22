package com.example.web_petvibe.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInCart {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String type;
    private Integer stock;
}