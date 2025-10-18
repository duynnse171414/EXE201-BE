package com.example.web_petvibe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    String description;

    @Column(nullable = false)
    Double price;

    @Column(nullable = false)
    Integer stock;

    @Column(name = "image_url", length = 1000)
    String imageUrl;

    @JsonIgnore
    @Column(name = "is_deleted")
    boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
