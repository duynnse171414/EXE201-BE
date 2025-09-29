package com.example.web_petvibe.model.reponse;

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
