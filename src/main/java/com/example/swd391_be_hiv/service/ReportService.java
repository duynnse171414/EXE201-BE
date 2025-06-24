package com.example.swd391_be_hiv.service;

import com.example.swd391_be_hiv.entity.Report;
import com.example.swd391_be_hiv.exception.DuplicateEntity;
import com.example.swd391_be_hiv.exception.NotFoundException;
import com.example.swd391_be_hiv.model.request.ReportRequest;
import com.example.swd391_be_hiv.model.reponse.ReportResponse;
import com.example.swd391_be_hiv.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    ReportRepository reportRepository;

    public ReportResponse createReport(ReportRequest requestDTO) {
        try {

            Report report = new Report();
            report.setGeneratedBy(requestDTO.getGeneratedBy());
            report.setReportType(requestDTO.getReportType());
            report.setContent(requestDTO.getContent());
            report.setGeneratedAt(LocalDateTime.now()); // Auto-set current time

            Report savedReport = reportRepository.save(report);
            return convertToResponseDTO(savedReport);

        } catch (Exception e) {
            throw new DuplicateEntity("Error creating report: " + e.getMessage());
        }
    }

    public List<ReportResponse> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ReportResponse updateReport(ReportRequest requestDTO, long reportId) {
        Report existingReport = reportRepository.findReportByReportId(reportId);
        if (existingReport == null) {
            throw new NotFoundException("Report not found");
        }


        existingReport.setGeneratedBy(requestDTO.getGeneratedBy());
        existingReport.setReportType(requestDTO.getReportType());
        existingReport.setContent(requestDTO.getContent());


        Report updatedReport = reportRepository.save(existingReport);
        return convertToResponseDTO(updatedReport);
    }

    public ReportResponse deleteReport(long reportId) {
        Report report = reportRepository.findReportByReportId(reportId);
        if (report == null) {
            throw new NotFoundException("Report not found");
        }

        ReportResponse response = convertToResponseDTO(report);
        reportRepository.delete(report);
        return response;
    }

    public ReportResponse getReportById(long reportId) {
        Report report = reportRepository.findReportByReportId(reportId);
        if (report == null) {
            throw new NotFoundException("Report not found");
        }
        return convertToResponseDTO(report);
    }

    public List<ReportResponse> getReportsByGeneratedBy(String generatedBy) {
        return reportRepository.findReportsByGeneratedBy(generatedBy)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ReportResponse> getReportsByType(String reportType) {
        return reportRepository.findReportsByReportType(reportType)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ReportResponse> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return reportRepository.findReportsByGeneratedAtBetween(startDate, endDate)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private ReportResponse convertToResponseDTO(Report report) {
        ReportResponse dto = new ReportResponse();
        dto.setReportId(report.getReportId());
        dto.setGeneratedBy(report.getGeneratedBy());
        dto.setReportType(report.getReportType());
        dto.setContent(report.getContent());
        dto.setGeneratedAt(report.getGeneratedAt());
        return dto;
    }
}