package com.example.web_petvibe.model.response;

import com.example.web_petvibe.entity.PetProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PetProfileResponse {
    private String message;
    private boolean success;
    private PetProfile data;
}