package com.self.finance.controller;

import com.self.finance.model.Budget;
import com.self.finance.service.BudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.self.finance.dto.BudgetReportDTO;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@Tag(name = "Budget Management", description = "APIs to manage monthly budgets per category")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public Budget setBudget(@RequestParam Long categoryId,
                            @RequestParam int month,
                            @RequestParam int year,
                            @RequestParam BigDecimal amount) {
        return budgetService.setBudget(categoryId, month, year, amount);
    }

    @GetMapping
    public List<Budget> getAll() {
        return budgetService.getAllBudgets();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }

    @GetMapping("/report")
    public List<BudgetReportDTO> getMonthlyReport(@RequestParam int month, @RequestParam int year) {
        return budgetService.generateMonthlyReport(month, year);
    }

}
