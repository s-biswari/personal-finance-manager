package com.self.finance.repository;

import com.self.finance.model.Budget;
import com.self.finance.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BudgetRepositoryTest {
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindAll() {
        Category category = Category.builder().name("BudgetCat").build();
        category = categoryRepository.save(category);
        Budget budget = Budget.builder()
                .category(category)
                .month(5)
                .year(2025)
                .amount(BigDecimal.valueOf(1000))
                .build();
        budgetRepository.save(budget);
        List<Budget> budgets = budgetRepository.findAll();
        assertFalse(budgets.isEmpty());
        assertTrue(budgets.stream().anyMatch(b -> b.getCategory().getName().equals("BudgetCat")));
    }
}
