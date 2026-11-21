package com.smartspend.smartspend.Service;
import com.smartspend.smartspend.dto.ExpenseRequestDTO;
import com.smartspend.smartspend.dto.ExpenseResponseDTO;
import com.smartspend.smartspend.entity.Expense;
import com.smartspend.smartspend.Repository.ExpenseRepository;
import com.smartspend.smartspend.entity.Expense;
import com.smartspend.smartspend.exception.ExpenseNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public ExpenseResponseDTO addExpense(ExpenseRequestDTO dto) {

        Expense expense = toEntity(dto);

        Expense savedExpense = expenseRepository.save(expense);

        return toResponseDTO(savedExpense);
    }

    public ExpenseResponseDTO updateExpense(
            Long id,
            ExpenseRequestDTO dto) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ExpenseNotFoundException(
                                "Expense not found with ID: " + id
                        )
                );

        expense.setAmount(dto.amount());
        expense.setCategory(dto.category());
        expense.setDescription(dto.description());
        expense.setDate(dto.date());

        Expense updatedExpense = expenseRepository.save(expense);

        return toResponseDTO(updatedExpense);
    }
    public ExpenseResponseDTO getExpenseById(Long id){

        Expense expense= expenseRepository.findById(id)
                .orElseThrow(()->
                        new ExpenseNotFoundException("Expense not found with ID : " + id));
        return toResponseDTO(expense);
    }

    public double getTotalExpenses()
    {
        return expenseRepository.getTotalExpenses();  }

    public List<ExpenseResponseDTO> getAllExpenses() {

        return expenseRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
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


    public List<ExpenseResponseDTO> getExpensesByCategory(String category) {

        return expenseRepository.findByCategoryIgnoreCase(category)
                .stream()
                .map(this::toResponseDTO)
                .toList();

    }

    public void deleteExpense(Long id) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ExpenseNotFoundException(
                                "Expense not found with ID: " + id
                        )
                );

        expenseRepository.delete(expense);
    }
    public List<Expense> getExpensesByDateRange(
            LocalDate startDate,
            LocalDate endDate) {

        return expenseRepository.findByDateBetween(
                startDate, endDate
        );
    }


    public Page<Expense> getExpensesWithPagination(int page, int size, String sortBy, String direction){
        Sort.Direction sortDirection=Sort.Direction.fromString(direction);
        Pageable pageable= PageRequest.of(
                page,
                size,
                Sort.by(sortDirection,sortBy)
        );
        return expenseRepository.findAll(pageable);

    }

    public List<Expense> getExpenseAboveAmount(int amount){
        return expenseRepository.findByAmountGreaterThan(amount);
    }

    private Expense toEntity(ExpenseRequestDTO dto) {

        Expense expense = new Expense();

        expense.setAmount(dto.amount());
        expense.setCategory(dto.category());
        expense.setDescription(dto.description());
        expense.setDate(dto.date());

        return expense;
    }

    private ExpenseResponseDTO toResponseDTO(Expense expense) {

        return new ExpenseResponseDTO(
                expense.getId(),
                expense.getAmount(),
                expense.getCategory(),
                expense.getDescription(),
                expense.getDate()
        );}
        public List<ExpenseResponseDTO> getSortedExpenses(
                String sortBy,
                String direction) {

            Sort.Direction sortDirection =
                    Sort.Direction.fromString(direction);

            Sort sort = Sort.by(sortDirection, sortBy);

            return expenseRepository.findAll(sort)
                    .stream()
                    .map(this::toResponseDTO)
                    .toList();
        }
    public Page<ExpenseResponseDTO> getPaginatedExpenses(
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);

        return expenseRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }
}
