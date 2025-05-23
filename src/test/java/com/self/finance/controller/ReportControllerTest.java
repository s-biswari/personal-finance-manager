package com.self.finance.controller;

import com.self.finance.dto.SpendingReportDTO;
import com.self.finance.service.ReportService;
import com.self.finance.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportController.class)
class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // Deprecated but required for test slice mocking in Spring Boot 3.4.x
    private ReportService reportService;
    @MockBean // Deprecated but required for test slice mocking in Spring Boot 3.4.x
    private TransactionService transactionService;

    @Test
    void testDownloadReportExcel() throws Exception {
        Mockito.when(transactionService.getSpendingByCategory(anyInt(), anyInt())).thenReturn(Collections.singletonList(new SpendingReportDTO(1L, "Groceries", 100.0)));
        mockMvc.perform(get("/download-report?month=5&year=2025&format=excel"))
                .andExpect(status().isOk());
    }

    @Test
    void testDownloadReportPdf() throws Exception {
        Mockito.when(transactionService.getSpendingByCategory(anyInt(), anyInt())).thenReturn(Collections.singletonList(new SpendingReportDTO(1L, "Groceries", 100.0)));
        mockMvc.perform(get("/download-report?month=5&year=2025&format=pdf"))
                .andExpect(status().isOk());
    }

    @Test
    void testDownloadReportInvalidFormat() throws Exception {
        Mockito.when(transactionService.getSpendingByCategory(anyInt(), anyInt())).thenReturn(Collections.singletonList(new SpendingReportDTO(1L, "Groceries", 100.0)));
        mockMvc.perform(get("/download-report?month=5&year=2025&format=invalid"))
                .andExpect(status().isBadRequest());
    }
}
