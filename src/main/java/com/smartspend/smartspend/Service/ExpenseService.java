package com.smartspend.smartspend.Service;

import com.smartspend.smartspend.Repository.ExpenseRepository;
import com.smartspend.smartspend.entity.Expense;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    public ExpenseService(ExpenseRepository expenseRepository)
    {
        this.expenseRepository=expenseRepository;
    }

    public Expense addExpense(Expense expense)
    {
        return expenseRepository.save(expense);
    }
    public List<Expense> getAllExpenses(){

        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id){
        return expenseRepository.getReferenceById(id);
    }

    public double getTotalExpenses()
    {
        return expenseRepository.getTotalExpenses();  }

    public Expense updateExpense(Long id,Expense updatedExpense)
    {
        Expense existingExpense = expenseRepository.findById(id).orElse(null);
        if(existingExpense == null)
            return null;
        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDescription(updatedExpense.getDescription());
        existingExpense.setDate(updatedExpense.getDate());

        return expenseRepository.save(existingExpense);
    }

    public boolean deleteExpense(Long id)
    {
        if (!expenseRepository.existsById(id)) {
            return false;
        }
        expenseRepository.deleteById(id);
        return true;
    }
    public List<Expense> getExpensesByCategory(String category) {

        return expenseRepository.findByCategoryIgnoreCase(category);

    }

    public List<Expense> getExpensesByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        return expenseRepository.findByDateBetween(
                startDate, endDate
        );
    }

    public List<Expense> getSortedExpenses(String sortBy,String direction)
    {
        Sort.Direction sortDirection= Sort.Direction.fromString(direction);
        Sort sort=Sort.by(sortDirection,sortBy);
        return expenseRepository.findAll(sort);
    }
}
