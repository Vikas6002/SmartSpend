package com.smartspend.smartspend.controller;
import com.smartspend.smartspend.dto.ExpenseRequestDTO;
import com.smartspend.smartspend.dto.ExpenseResponseDTO;
import com.smartspend.smartspend.Service.ExpenseService;
import com.smartspend.smartspend.entity.Expense;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ExpenseResponseDTO>> getByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
                expenseService.getExpensesByCategory(category)
        );
    }


    @GetMapping("/sorted")
    public List<ExpenseResponseDTO> getSortedExpenses(
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        return expenseService.getSortedExpenses(sortBy, direction);
    }



    @GetMapping("/above")
    public List<Expense> getExpensesAboveAmount(@RequestParam(defaultValue = "0") int amount){
        return expenseService.getExpenseAboveAmount(amount);

    }
    // CREATE
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> addExpense(
            @Valid @RequestBody ExpenseRequestDTO dto) {

        ExpenseResponseDTO saved = expenseService.addExpense(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    // READ ALL
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses() {

        return ResponseEntity.ok(expenseService.getAllExpenses());
    }


    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(
            @PathVariable Long id) {

        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }


    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseRequestDTO dto) {

        return ResponseEntity.ok(expenseService.updateExpense(id, dto));
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable Long id) {

        expenseService.deleteExpense(id);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/page")
    public ResponseEntity<Page<ExpenseResponseDTO>> getPaginatedExpenses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(
                expenseService.getPaginatedExpenses(page, size)
        );
    }
}
