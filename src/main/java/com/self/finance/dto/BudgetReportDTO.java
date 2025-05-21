package com.self.finance.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetReportDTO {
    private String categoryName;
    private int month;
    private int year;
    private BigDecimal budgetAmount;
    private BigDecimal actualSpent;
    private boolean overspent;
}
