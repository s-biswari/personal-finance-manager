package com.self.finance.repository;

import com.self.finance.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT t.category_id, SUM(t.amount) " +
            "FROM transactions t " +
            "WHERE EXTRACT(MONTH FROM t.date) = :month " +
            "AND EXTRACT(YEAR FROM t.date) = :year " +
            "GROUP BY t.category_id", nativeQuery = true)
    List<Object[]> findSpendingByCategory(@Param("month") int month, @Param("year") int year);

    @Query("SELECT t.category.id, t.category.name, SUM(t.amount) " +
            "FROM Transaction t " +
            "WHERE EXTRACT(MONTH FROM t.date) = :month AND EXTRACT(YEAR FROM t.date) = :year " +
            "GROUP BY t.category.id, t.category.name")
    List<Object[]> findSpendingByCategoryForExport(@Param("month") int month, @Param("year") int year);


}
