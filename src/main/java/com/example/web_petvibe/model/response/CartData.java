package com.example.web_petvibe.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartData {
    private Long id;
    private Integer quantity;
    private Double total;
    private Long userId;
    private ProductInCart product;
}