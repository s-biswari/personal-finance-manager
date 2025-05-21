package com.self.finance.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequestDTO {
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private Long categoryId;
}
