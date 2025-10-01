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
    String petAge;
    String petName;
    String petSize;
    String petType;
    String token;
    Long customerId;

}
