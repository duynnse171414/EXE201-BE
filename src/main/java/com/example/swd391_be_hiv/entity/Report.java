package com.example.swd391_be_hiv.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Report_ID")
     Long reportId;

    @Column(name = "Generated_by")
     String generatedBy;

    @Column(name = "Report_type")
     String reportType;

    @Column(name = "Content", columnDefinition = "TEXT")
     String content;

    @Column(name = "Generated_at")
     LocalDateTime generatedAt;
}
