package com.self.finance.controller;

import com.self.finance.dto.SpendingReportDTO;
import com.self.finance.service.ReportService;
import com.self.finance.service.TransactionService; // This service is responsible for fetching the spending data.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private TransactionService transactionService; // Service to fetch data from DB

    // Endpoint to download the spending report
    @GetMapping("/download-report")
    public void downloadReport(@RequestParam int month, @RequestParam int year,
                               @RequestParam String format, HttpServletResponse response) throws IOException {
        List<SpendingReportDTO> reportData = transactionService.getSpendingByCategory(month, year);

        if ("pdf".equalsIgnoreCase(format)) {
            reportService.generatePdfReport(response, reportData);
        } else
            if ("excel".equalsIgnoreCase(format)) {
            reportService.generateExcelReport(response, reportData);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid format. Please specify 'pdf' or 'excel'.");
        }
    }
}
