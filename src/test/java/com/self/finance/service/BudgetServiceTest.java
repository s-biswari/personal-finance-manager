package com.self.finance.service;

import com.self.finance.dto.BudgetReportDTO;
import com.self.finance.model.Budget;
import com.self.finance.model.Category;
import com.self.finance.repository.BudgetRepository;
import com.self.finance.repository.CategoryRepository;
import com.self.finance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceTest {
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BudgetService budgetService;

    private Category category;
    private Budget budget;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = Category.builder().id(1L).name("Groceries").build();
        budget = Budget.builder().id(1L).category(category).month(5).year(2025).amount(BigDecimal.valueOf(500)).build();
    }

    @Test
    void testSetBudget_New() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(budgetRepository.findByCategoryAndMonthAndYear(category, 5, 2025)).thenReturn(Optional.empty());
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
        Budget result = budgetService.setBudget(1L, 5, 2025, BigDecimal.valueOf(500));
        assertEquals("Groceries", result.getCategory().getName());
        assertEquals(BigDecimal.valueOf(500), result.getAmount());
    }

    @Test
    void testSetBudget_Update() {
        Budget existing = Budget.builder().id(1L).category(category).month(5).year(2025).amount(BigDecimal.valueOf(100)).build();
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(budgetRepository.findByCategoryAndMonthAndYear(category, 5, 2025)).thenReturn(Optional.of(existing));
        when(budgetRepository.save(any(Budget.class))).thenReturn(budget);
        Budget result = budgetService.setBudget(1L, 5, 2025, BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500), result.getAmount());
    }

    @Test
    void testGetAllBudgets() {
        when(budgetRepository.findAll()).thenReturn(Arrays.asList(budget));
        List<Budget> result = budgetService.getAllBudgets();
        assertEquals(1, result.size());
        assertEquals("Groceries", result.get(0).getCategory().getName());
    }

    @Test
    void testDeleteBudget() {
        doNothing().when(budgetRepository).deleteById(1L);
        budgetService.deleteBudget(1L);
        verify(budgetRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGenerateMonthlyReport() {
        when(budgetRepository.findAll()).thenReturn(Arrays.asList(budget));
        Object[] spendingRow = new Object[]{1L, BigDecimal.valueOf(600)};
        when(transactionRepository.findSpendingByCategory(5, 2025)).thenReturn(Arrays.<Object[]>asList(spendingRow));
        List<BudgetReportDTO> report = budgetService.generateMonthlyReport(5, 2025);
        assertEquals(1, report.size());
        assertEquals("Groceries", report.get(0).getCategoryName());
        assertTrue(report.get(0).isOverspent());
    }
}
