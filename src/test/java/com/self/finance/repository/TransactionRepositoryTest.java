package com.self.finance.repository;

import com.self.finance.model.Transaction;
import com.self.finance.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindAll() {
        Category category = Category.builder().name("TestCat").build();
        category = categoryRepository.save(category);
        Transaction tx = Transaction.builder()
                .description("TestTx")
                .amount(BigDecimal.valueOf(123.45))
                .date(LocalDate.now())
                .category(category)
                .build();
        transactionRepository.save(tx);
        List<Transaction> txs = transactionRepository.findAll();
        assertFalse(txs.isEmpty());
        assertTrue(txs.stream().anyMatch(t -> "TestTx".equals(t.getDescription())));
    }
}
