package com.self.finance.service;

import com.self.finance.dto.SpendingReportDTO;
import com.self.finance.dto.TransactionRequestDTO;
import com.self.finance.dto.TransactionResponseDTO;
import com.self.finance.model.Category;
import com.self.finance.model.Transaction;
import com.self.finance.repository.CategoryRepository;
import com.self.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<TransactionResponseDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TransactionResponseDTO getTransactionById(Long id) {
        return convertToDTO(transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found")));
    }

    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction transaction = Transaction.builder()
                .amount(dto.getAmount())
                .date(dto.getDate())
                .description(dto.getDescription())
                .category(category)
                .build();

        return convertToDTO(transactionRepository.save(transaction));
    }

    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO dto) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        transaction.setDescription(dto.getDescription());
        transaction.setAmount(dto.getAmount());
        transaction.setDate(dto.getDate());
        transaction.setCategory(category);

        return convertToDTO(transactionRepository.save(transaction));
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    private TransactionResponseDTO convertToDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getAmount());
        dto.setDate(transaction.getDate());
        dto.setCategoryName(transaction.getCategory().getName());
        return dto;
    }

    public List<SpendingReportDTO> getSpendingByCategory(int month, int year) {
        List<Object[]> result = transactionRepository.findSpendingByCategoryForExport(month, year);

        // Convert the result from Object[] to SpendingReportDTO
        return result.stream()
                .map(row -> new SpendingReportDTO(
                        (Long) row[0],          // categoryId
                        (String) row[1],        // categoryName
                        ((BigDecimal) row[2]).doubleValue()        // totalAmount
                ))
                .collect(Collectors.toList());
    }
}
