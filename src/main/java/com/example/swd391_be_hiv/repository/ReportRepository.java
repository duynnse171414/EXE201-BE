package com.example.swd391_be_hiv.repository;

import com.example.swd391_be_hiv.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findReportByReportId(Long reportId);

    List<Report> findReportsByGeneratedBy(String generatedBy);

    List<Report> findReportsByReportType(String reportType);

    List<Report> findReportsByGeneratedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Report> findReportsByGeneratedAtAfter(LocalDateTime date);

    List<Report> findReportsByGeneratedAtBefore(LocalDateTime date);

    List<Report> findReportsByReportTypeAndGeneratedBy(String reportType, String generatedBy);
}