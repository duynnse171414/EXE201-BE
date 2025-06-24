package com.example.swd391_be_hiv.api;

import com.example.swd391_be_hiv.model.request.ReportRequest;
import com.example.swd391_be_hiv.model.reponse.ReportResponse;
import com.example.swd391_be_hiv.service.ReportService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@SecurityRequirement(name = "api")
@RestController
@RequestMapping("/api/report")
public class ReportAPI {

    @Autowired
    ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> createReport(@Valid @RequestBody ReportRequest requestDTO) {
        ReportResponse response = reportService.createReport(requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("getReports")
    public ResponseEntity<List<ReportResponse>> getReports() {
        List<ReportResponse> reportList = reportService.getAllReports();
        return ResponseEntity.ok(reportList);
    }

    @GetMapping("{id}")
    public ResponseEntity<ReportResponse> getReportById(@PathVariable long id) {
        ReportResponse response = reportService.getReportById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<ReportResponse> updateReport(@Valid @RequestBody ReportRequest requestDTO, @PathVariable long id) {
        ReportResponse response = reportService.updateReport(requestDTO, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ReportResponse> deleteReport(@PathVariable long id) {
        ReportResponse response = reportService.deleteReport(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generated-by/{generatedBy}")
    public ResponseEntity<List<ReportResponse>> getReportsByGeneratedBy(@PathVariable String generatedBy) {
        List<ReportResponse> reports = reportService.getReportsByGeneratedBy(generatedBy);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/type/{reportType}")
    public ResponseEntity<List<ReportResponse>> getReportsByType(@PathVariable String reportType) {
        List<ReportResponse> reports = reportService.getReportsByType(reportType);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ReportResponse>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ReportResponse> reports = reportService.getReportsByDateRange(startDate, endDate);
        return ResponseEntity.ok(reports);
    }
}