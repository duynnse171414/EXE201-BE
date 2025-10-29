package com.example.web_petvibe.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequest {

    private String fullName;

    @Email(message = "Invalid Email!")
    private String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone number!")
    private String phone;


}
