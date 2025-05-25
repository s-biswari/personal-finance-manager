package com.self.finance.controller;

import com.self.finance.config.TestConfig;
import com.self.finance.dto.TransactionRequestDTO;
import com.self.finance.dto.TransactionResponseDTO;
import com.self.finance.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(TestConfig.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // Deprecated but required for test slice mocking in Spring Boot 3.4.x
    private TransactionService transactionService;

    @Test
    @WithMockUser
    void testGetAll() throws Exception {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(1L);
        dto.setDescription("Test");
        Mockito.when(transactionService.getAllTransactions()).thenReturn(Collections.singletonList(dto));
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test"));
    }

    @Test
    @WithMockUser
    void testGetById() throws Exception {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(1L);
        dto.setDescription("Test");
        Mockito.when(transactionService.getTransactionById(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreate() throws Exception {
        TransactionRequestDTO req = new TransactionRequestDTO();
        req.setDescription("Test");
        req.setAmount(BigDecimal.valueOf(100));
        req.setDate(LocalDate.now());
        req.setCategoryId(1L);
        TransactionResponseDTO resp = new TransactionResponseDTO();
        resp.setId(1L);
        resp.setDescription("Test");
        Mockito.when(transactionService.createTransaction(any(TransactionRequestDTO.class))).thenReturn(resp);
        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Test\",\"amount\":100,\"date\":\"2025-05-23\",\"categoryId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Test"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdate() throws Exception {
        TransactionResponseDTO resp = new TransactionResponseDTO();
        resp.setId(1L);
        resp.setDescription("Updated");
        Mockito.when(transactionService.updateTransaction(any(Long.class), any(TransactionRequestDTO.class))).thenReturn(resp);
        mockMvc.perform(put("/api/transactions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Updated\",\"amount\":200,\"date\":\"2025-05-23\",\"categoryId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/transactions/1"))
                .andExpect(status().isOk());
    }
}
