package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class EducationContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Post_ID")
     Long postId;

    @Column(name = "Title")
     String title;

    @Column(name = "Content", columnDefinition = "TEXT")
     String content;

    @Column(name = "Image")
    String image;

    @ManyToOne
    @JoinColumn(name = "Staff_ID")
     Staff staff;

    @Column(name = "Created_at")
     LocalDateTime createdAt;
}