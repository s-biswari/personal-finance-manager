package com.self.finance.controller;

import com.self.finance.dto.TransactionRequestDTO;
import com.self.finance.dto.TransactionResponseDTO;
import com.self.finance.service.TransactionService;
import com.self.finance.dto.SpendingReportDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Management", description = "CRUD APIs for financial transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionResponseDTO> getAll() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public TransactionResponseDTO getById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PostMapping
    public TransactionResponseDTO create(@RequestBody TransactionRequestDTO dto) {
        return transactionService.createTransaction(dto);
    }

    @PutMapping("/{id}")
    public TransactionResponseDTO update(@PathVariable Long id, @RequestBody TransactionRequestDTO dto) {
        return transactionService.updateTransaction(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
    }

    @GetMapping("/spending")
    public List<SpendingReportDTO> getSpendingByCategory(@RequestParam int month, @RequestParam int year) {
        return transactionService.getSpendingByCategory(month, year);
    }
}
