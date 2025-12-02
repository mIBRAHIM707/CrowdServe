package com.crowdserve.controller;

import com.crowdserve.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Using @RestController since we are returning data/files, not a view name
@RequestMapping("/reports")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    // Endpoint to download the Completed Tasks Report
    @GetMapping("/completed-tasks")
    public ResponseEntity<byte[]> downloadCompletedTasksReport() {
        // 1. Generate the report data (byte array) from the service
        byte[] reportData = reportService.generateCompletedTasksReport();

        // 2. Set headers to tell the browser this is a downloadable CSV file
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "completed_tasks_report.csv");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        // 3. Return the ResponseEntity with status 200 OK, headers, and the data
        return ResponseEntity.ok()
                .headers(headers)
                .body(reportData);
    }
    
    // Endpoint to download the Top Contributors Report
    @GetMapping("/top-contributors")
    public ResponseEntity<byte[]> downloadTopContributorsReport() {
        // 1. Generate the report data
        byte[] reportData = reportService.generateTopContributorsReport();

        // 2. Set headers for the file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "top_contributors_report.csv");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        // 3. Return the response
        return ResponseEntity.ok()
                .headers(headers)
                .body(reportData);
    }
}