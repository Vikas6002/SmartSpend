package com.smartspend.smartspend.controller;

import com.smartspend.smartspend.Service.ExpenseService;
import com.smartspend.smartspend.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService)
    {
        this.expenseService=expenseService;
    }
//CREATE
    @PostMapping
    public Expense abbExpense(@RequestBody Expense expense)
    {
        return expenseService.addExpense(expense);
    }

    //ReadALl
    @GetMapping
    public List<Expense> getAllExpense()
    {
        return expenseService.getAllExpenses();
    }
    @GetMapping("/{id}")
    public Expense getExpensebyId(@PathVariable Long id)
    {
        return expenseService.getExpenseById(id);
    }

    @PutMapping("/{id}")
    public Expense updateExpense(@PathVariable Long id,@RequestBody Expense expense)
    {
        return expenseService.updateExpense(id,expense);
    }

    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id)
    {
        boolean deleted=expenseService.deleteExpense(id);
        if(deleted)
            return "Deleted";
        else return "Not Deleted";
    }

    @GetMapping("/date-range")
    public List<Expense> getExpensesByDateRange(@RequestParam("startDate")LocalDate startDate,@RequestParam("endDate") LocalDate endDate)
    {
        return expenseService.getExpensesByDateRange(startDate,endDate);
    }

    @GetMapping("/total")
    public Double getTotalAmount() {
        return expenseService.getTotalExpenses();
    }

    @GetMapping("/category/{category}")
    public List<Expense> getExpenseByCstegory(@PathVariable String category)
    {
        return expenseService.getExpensesByCategory(category);
    }


    @GetMapping("/sorted")
    public List<Expense> getSortedExpenses(
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return expenseService.getSortedExpenses(sortBy, direction);
    }

    @GetMapping("/page")
    public Page<Expense> getExpensesWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return expenseService.getExpensesWithPagination(
                page, size, sortBy, direction
        );
    }

    @GetMapping("/above")
    public List<Expense> getExpensesAboveAmount(@RequestParam(defaultValue = "0") int amount){
        return expenseService.getExpenseAboveAmount(amount);

    }
}
