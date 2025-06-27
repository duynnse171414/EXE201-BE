package com.example.swd391_be_hiv.model.reponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EducationContentResponse {

    private Long postId;
    private String title;
    private String content;
    private Long staffId;
    private String staffName;
    private String image;
    private LocalDateTime createdAt;
}