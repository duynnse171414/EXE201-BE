package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Entity
@Getter
@Setter
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Staff_ID")
     Long staffId;

    @Column(name = "Name")
     String name;

    @Column(name = "Email")
     String email;

    @Column(name = "Phone")
     String phone;

    @Column(name = "Gender")
     String gender;

    @Column(name = "Password")
     String password;

    @Column(name = "Is_deleted")
     Boolean isDeleted;

    @OneToMany(mappedBy = "staff")
     List<EducationContent> educationContents;

    @OneToMany(mappedBy = "staff")
     List<Blog> blogs;
}