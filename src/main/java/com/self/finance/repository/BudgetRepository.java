package com.self.finance.repository;

import com.self.finance.model.Budget;
import com.self.finance.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByCategoryAndMonthAndYear(Category category, int month, int year);
}
