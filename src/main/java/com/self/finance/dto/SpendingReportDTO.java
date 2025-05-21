package com.self.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpendingReportDTO {
    private Long categoryId;
    private String categoryName;
    private Double totalAmount;
}
