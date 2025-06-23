package com.example.swd391_be_hiv.model.reponse;

import lombok.Data;

@Data
public class StaffResponse {

    private Long staffId;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private Boolean isDeleted;
    // Password is not included in response for security reasons
}