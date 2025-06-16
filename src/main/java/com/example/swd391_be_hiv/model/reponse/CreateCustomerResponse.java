package com.example.swd391_be_hiv.model.reponse;

import com.example.swd391_be_hiv.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCustomerResponse {
    private String message;
    private boolean success;
    private Customer data;
}
