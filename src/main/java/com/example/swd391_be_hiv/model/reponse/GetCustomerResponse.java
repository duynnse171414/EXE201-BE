package com.example.swd391_be_hiv.model.reponse;

import com.example.swd391_be_hiv.entity.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetCustomerResponse {
    private String message;
    private boolean success;
    private Account data;
}
