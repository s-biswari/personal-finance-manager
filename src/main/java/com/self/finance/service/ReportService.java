package com.self.finance.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.self.finance.dto.SpendingReportDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    // Generate Excel report
    public void generateExcelReport(HttpServletResponse response, List<SpendingReportDTO> reportData) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Spending Report");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Category ID");
            headerRow.createCell(1).setCellValue("Category Name");
            headerRow.createCell(2).setCellValue("Total Amount");

            // Populate data rows
            int rowNum = 1;
            for (SpendingReportDTO data : reportData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(data.getCategoryId());
                row.createCell(1).setCellValue(data.getCategoryName());
                row.createCell(2).setCellValue(data.getTotalAmount());
            }

            // Set response headers
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=spending_report.xlsx");

            // Write to response output stream
            workbook.write(response.getOutputStream());
        }
    }

    // Generate PDF report
    public void generatePdfReport(HttpServletResponse response, List<SpendingReportDTO> reportData) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title
            Paragraph title = new Paragraph("Spending Report")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold();
            document.add(title);

            // Add table
            float[] columnWidths = {1, 5, 2};
            Table table = new Table(columnWidths);
            table.addHeaderCell("Category ID");
            table.addHeaderCell("Category Name");
            table.addHeaderCell("Total Amount");

            for (SpendingReportDTO data : reportData) {
                table.addCell(String.valueOf(data.getCategoryId()));
                table.addCell(data.getCategoryName());
                table.addCell(String.valueOf(data.getTotalAmount()));
            }

            document.add(table);
            document.close();

            // Set response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=spending_report.pdf");

            // Write to response output stream
            response.getOutputStream().write(baos.toByteArray());
        }
    }
}
