package com.example.swd391_be_hiv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long Id;

    @JsonIgnore
    @Column(name = "is_deleted")
     boolean isDeleted = false ;

    @OneToOne  // Thay đổi từ @ManyToOne thành @OneToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    Account account;




}