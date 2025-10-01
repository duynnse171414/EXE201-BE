package com.example.web_petvibe.model.request;

import com.example.web_petvibe.entity.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RegisterRequest {
//    @NotBlank
    String fullName;

//    @NotBlank(message = "Code can not be blank!")
//    @Pattern(regexp = "^(Male|Female)$", message = ("Invalid Gender"))
//    String Gender;

//    @Email(message = "Invalid Email!")
    @Column(unique = true)
    String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    String phone;

    @Size(min = 6, message = "Password must be at least 6 character!")
    String password;

    @Enumerated(EnumType.STRING)
    Role role;

    String petName;

    String petAge;

    String petType;

    String petSize;
}
