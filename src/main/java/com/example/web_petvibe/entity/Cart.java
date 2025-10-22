package com.example.web_petvibe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    Integer quantity;

    @Column(nullable = false)
    Double total;

    @Column(name = "product_id", nullable = false)
    Long productId;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @JsonIgnore
    @Column(name = "is_deleted")
    boolean isDeleted = false;
}