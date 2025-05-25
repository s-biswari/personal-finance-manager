package com.self.finance.service;

import com.self.finance.dto.SpendingReportDTO;
import com.self.finance.dto.TransactionRequestDTO;
import com.self.finance.dto.TransactionResponseDTO;
import com.self.finance.model.Category;
import com.self.finance.model.Transaction;
import com.self.finance.repository.CategoryRepository;
import com.self.finance.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Category category;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(1L).name("Groceries").build();
        transaction = Transaction.builder()
                .id(1L)
                .description("Test Transaction")
                .amount(BigDecimal.valueOf(100))
                .date(LocalDate.now())
                .category(category)
                .build();
    }

    @Test
    void testGetAllTransactions() {
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction));
        List<TransactionResponseDTO> result = transactionService.getAllTransactions();
        assertEquals(1, result.size());
        assertEquals("Test Transaction", result.get(0).getDescription());
    }

    @Test
    void testGetTransactionById() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        TransactionResponseDTO dto = transactionService.getTransactionById(1L);
        assertEquals("Test Transaction", dto.getDescription());
        assertEquals("Groceries", dto.getCategoryName());
    }

    @Test
    void testCreateTransaction() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setDescription("New Transaction");
        dto.setAmount(BigDecimal.valueOf(200));
        dto.setDate(LocalDate.now());
        dto.setCategoryId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> {
            Transaction t = i.getArgument(0);
            t.setId(2L);
            return t;
        });
        TransactionResponseDTO response = transactionService.createTransaction(dto);
        assertEquals("New Transaction", response.getDescription());
        assertEquals("Groceries", response.getCategoryName());
    }

    @Test
    void testUpdateTransaction() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setDescription("Updated Transaction");
        dto.setAmount(BigDecimal.valueOf(300));
        dto.setDate(LocalDate.now());
        dto.setCategoryId(1L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArgument(0));
        TransactionResponseDTO response = transactionService.updateTransaction(1L, dto);
        assertEquals("Updated Transaction", response.getDescription());
        assertEquals(BigDecimal.valueOf(300), response.getAmount());
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(transactionRepository).deleteById(1L);
        transactionService.deleteTransaction(1L);
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetSpendingByCategory() {
        Object[] row = new Object[]{1L, "Groceries", BigDecimal.valueOf(500)};
        when(transactionRepository.findSpendingByCategoryForExport(5, 2025)).thenReturn(Arrays.<Object[]>asList(row));
        List<SpendingReportDTO> report = transactionService.getSpendingByCategory(5, 2025);
        assertEquals(1, report.size());
        assertEquals("Groceries", report.get(0).getCategoryName());
        assertEquals(500.0, report.get(0).getTotalAmount());
    }

    @Test
    void testUpdateTransaction_NotFound() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setDescription("Updated Transaction");
        dto.setAmount(BigDecimal.valueOf(300));
        dto.setDate(LocalDate.now());
        dto.setCategoryId(1L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> transactionService.updateTransaction(1L, dto));
        assertTrue(ex.getMessage().contains("Transaction not found"));
    }

    @Test
    void testUpdateTransaction_CategoryNotFound() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setDescription("Updated Transaction");
        dto.setAmount(BigDecimal.valueOf(300));
        dto.setDate(LocalDate.now());
        dto.setCategoryId(1L);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> transactionService.updateTransaction(1L, dto));
        assertTrue(ex.getMessage().contains("Category not found"));
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> transactionService.getTransactionById(2L));
        assertTrue(ex.getMessage().contains("Transaction not found"));
    }

    @Test
    void testCreateTransaction_CategoryNotFound() {
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setDescription("New Transaction");
        dto.setAmount(BigDecimal.valueOf(200));
        dto.setDate(LocalDate.now());
        dto.setCategoryId(2L);
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> transactionService.createTransaction(dto));
        assertTrue(ex.getMessage().contains("Category not found"));
    }
}
