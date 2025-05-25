package com.self.finance.controller;

import com.self.finance.config.TestConfig;
import com.self.finance.model.Budget;
import com.self.finance.dto.BudgetReportDTO;
import com.self.finance.service.BudgetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BudgetController.class)
@Import(TestConfig.class)
class BudgetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean // Deprecated but required for test slice mocking in Spring Boot 3.4.x
    private BudgetService budgetService;

    @Test
    @WithMockUser
    void testGetAll() throws Exception {
        Budget budget = Budget.builder().id(1L).amount(BigDecimal.valueOf(1000)).build();
        Mockito.when(budgetService.getAllBudgets()).thenReturn(Collections.singletonList(budget));
        mockMvc.perform(get("/api/budgets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSetBudget() throws Exception {
        Budget budget = Budget.builder().id(1L).amount(BigDecimal.valueOf(1000)).build();
        Mockito.when(budgetService.setBudget(anyLong(), any(Integer.class), any(Integer.class), any(BigDecimal.class))).thenReturn(budget);
        mockMvc.perform(post("/api/budgets?categoryId=1&month=5&year=2025&amount=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/budgets/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetMonthlyReport() throws Exception {
        BudgetReportDTO dto = BudgetReportDTO.builder().categoryName("Groceries").budgetAmount(BigDecimal.valueOf(1000)).actualSpent(BigDecimal.valueOf(900)).month(5).year(2025).overspent(false).build();
        Mockito.when(budgetService.generateMonthlyReport(5, 2025)).thenReturn(Collections.singletonList(dto));
        mockMvc.perform(get("/api/budgets/report?month=5&year=2025"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].categoryName").value("Groceries"));
    }
}
