package com.smartspend.smartspend.Repository;

import com.smartspend.smartspend.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e")
    Double getTotalExpenses();
    List<Expense> findByCategoryIgnoreCase(String category);
    List<Expense> findByDateBetween(LocalDate startDate,LocalDate endDate);

}
