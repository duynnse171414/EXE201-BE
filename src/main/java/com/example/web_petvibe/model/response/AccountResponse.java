package com.example.web_petvibe.model.response;

import lombok.Data;

import java.util.List;

@Data
public class AccountResponse {
    long id;
    String FullName;
    String email;
    String phone;
    String role;
    String token;
    List<Long> petIds;
    List<String> petNames;

}
