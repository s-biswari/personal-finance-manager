package com.self.finance.service;

import com.self.finance.model.Budget;
import com.self.finance.model.Category;
import com.self.finance.repository.BudgetRepository;
import com.self.finance.repository.CategoryRepository;
import com.self.finance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.self.finance.dto.BudgetReportDTO;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository,
                        CategoryRepository categoryRepository,
                        TransactionRepository transactionRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public Budget setBudget(Long categoryId, int month, int year, BigDecimal amount) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return budgetRepository.findByCategoryAndMonthAndYear(category, month, year)
                .map(budget -> {
                    budget.setAmount(amount);
                    return budgetRepository.save(budget);
                })
                .orElseGet(() -> budgetRepository.save(
                        Budget.builder()
                                .category(category)
                                .month(month)
                                .year(year)
                                .amount(amount)
                                .build()));
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<BudgetReportDTO> generateMonthlyReport(int month, int year) {
        // Step 1: Fetch all budgets for the month
        List<Budget> budgets = budgetRepository.findAll().stream()
                .filter(b -> b.getMonth() == month && b.getYear() == year)
                .toList();

        // Step 2: Get total spending per category
        List<Object[]> spendings = transactionRepository.findSpendingByCategory(month, year);
        Map<Long, BigDecimal> spendingMap = spendings.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (BigDecimal) row[1]
                ));

        // Step 3: Build report
        List<BudgetReportDTO> report = new ArrayList<>();
        for (Budget budget : budgets) {
            BigDecimal spent = spendingMap.getOrDefault(budget.getCategory().getId(), BigDecimal.ZERO);
            boolean overspent = spent.compareTo(budget.getAmount()) > 0;

            report.add(BudgetReportDTO.builder()
                    .categoryName(budget.getCategory().getName())
                    .month(month)
                    .year(year)
                    .budgetAmount(budget.getAmount())
                    .actualSpent(spent)
                    .overspent(overspent)
                    .build());
        }

        return report;
    }

}
