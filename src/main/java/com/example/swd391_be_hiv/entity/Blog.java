package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter

public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Blog_ID")
     Long blogId;

    @Column(name = "Title")
     String title;

    @Column(name = "Image")
    String image;

    @ManyToOne
    @JoinColumn(name = "Staff_ID")
     Staff staff;

    @Column(name = "Content", columnDefinition = "TEXT")
     String content;

    @Column(name = "Create_Date")
     LocalDateTime createDate;
}