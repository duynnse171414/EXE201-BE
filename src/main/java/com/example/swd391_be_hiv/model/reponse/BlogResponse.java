package com.example.swd391_be_hiv.model.reponse;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BlogResponse {

    private Long blogId;
    private String title;
    private Long staffId;
    private String staffName;
    private String content;
    private String image;
    private LocalDateTime createDate;
}