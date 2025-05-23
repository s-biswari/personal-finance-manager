package com.self.finance.service;

import com.self.finance.dto.SpendingReportDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @InjectMocks
    private ReportService reportService;

    @Test
    void testGenerateExcelReport() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                // No-op for test: not needed for in-memory stream
            }
        });
        List<SpendingReportDTO> data = List.of(new SpendingReportDTO(1L, "Groceries", 100.0));
        assertDoesNotThrow(() -> reportService.generateExcelReport(response, data));
    }

    @Test
    void testGeneratePdfReport() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                // No-op for test: not needed for in-memory stream
            }
        });
        List<SpendingReportDTO> data = List.of(new SpendingReportDTO(1L, "Groceries", 100.0));
        assertDoesNotThrow(() -> reportService.generatePdfReport(response, data));
    }
}
