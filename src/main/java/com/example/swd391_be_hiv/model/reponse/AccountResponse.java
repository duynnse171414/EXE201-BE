package com.example.swd391_be_hiv.model.reponse;

import lombok.Data;

@Data
public class AccountResponse {
    long id;
    String FullName;
    String Gender;
    String email;
    String phone;
    String role;
    String token;
    Long customerId;

}
