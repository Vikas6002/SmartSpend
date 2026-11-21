
package com.smartspend.smartspend.Service;

import com.smartspend.smartspend.dto.ExpenseRequestDTO;
import com.smartspend.smartspend.dto.ExpenseResponseDTO;
import com.smartspend.smartspend.entity.Expense;
import com.smartspend.smartspend.exception.ExpenseNotFoundException;
import com.smartspend.smartspend.Repository.ExpenseRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense createExpense() {
        Expense expense = new Expense();
        expense.setId(1L);
        expense.setAmount(500);
        expense.setCategory("Food");
        expense.setDescription("Lunch");
        expense.setDate(LocalDate.of(2026, 11, 21));
        return expense;
    }

    // TEST 1: ADD EXPENSE
    @Test
    void testAddExpense() {

        ExpenseRequestDTO dto = new ExpenseRequestDTO(
                500.0,
                "Food",
                "Lunch",
                LocalDate.of(2026, 11, 21)
        );

        when(expenseRepository.save(any(Expense.class)))
                .thenAnswer(invocation -> {
                    Expense expense = invocation.getArgument(0);
                    expense.setId(1L);
                    return expense;
                });

        ExpenseResponseDTO result = expenseService.addExpense(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(500.0, result.amount());
        assertEquals("Food", result.category());

        verify(expenseRepository).save(any(Expense.class));
    }

    // TEST 2: GET EXPENSE BY ID
    @Test
    void testGetExpenseById() {

        Expense expense = createExpense();

        when(expenseRepository.findById(1L))
                .thenReturn(Optional.of(expense));

        ExpenseResponseDTO result =
                expenseService.getExpenseById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(500.0, result.amount());
        assertEquals("Food", result.category());

        verify(expenseRepository).findById(1L);
    }

    // TEST 3: GET ALL EXPENSES
    @Test
    void testGetAllExpenses() {

        Expense expense1 = createExpense();

        Expense expense2 = createExpense();
        expense2.setId(2L);
        expense2.setAmount(1000);
        expense2.setCategory("Shopping");

        when(expenseRepository.findAll())
                .thenReturn(List.of(expense1, expense2));

        List<ExpenseResponseDTO> result =
                expenseService.getAllExpenses();

        assertEquals(2, result.size());
        assertEquals("Food", result.get(0).category());
        assertEquals("Shopping", result.get(1).category());

        verify(expenseRepository).findAll();
    }

    // TEST 4: UPDATE EXPENSE
    @Test
    void testUpdateExpense() {

        Expense expense = createExpense();

        ExpenseRequestDTO dto = new ExpenseRequestDTO(
                1200.0,
                "Shopping",
                "Bought clothes",
                LocalDate.of(2026, 11, 21)
        );

        when(expenseRepository.findById(1L))
                .thenReturn(Optional.of(expense));

        when(expenseRepository.save(any(Expense.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ExpenseResponseDTO result =
                expenseService.updateExpense(1L, dto);

        assertEquals(1200.0, result.amount());
        assertEquals("Shopping", result.category());
        assertEquals("Bought clothes", result.description());

        verify(expenseRepository).save(expense);
    }

    // TEST 5: DELETE EXPENSE
    @Test
    void testDeleteExpense() {

        Expense expense = createExpense();

        when(expenseRepository.findById(1L))
                .thenReturn(Optional.of(expense));

        expenseService.deleteExpense(1L);

        verify(expenseRepository).findById(1L);
        verify(expenseRepository).delete(expense);
    }

    // TEST 6: EXPENSE NOT FOUND
    @Test
    void testExpenseNotFound() {

        when(expenseRepository.findById(999L))
                .thenReturn(Optional.empty());

        ExpenseNotFoundException exception =
                assertThrows(
                        ExpenseNotFoundException.class,
                        () -> expenseService.getExpenseById(999L)
                );

        assertEquals(
                "Expense not found with ID : 999",
                exception.getMessage()
        );

        verify(expenseRepository).findById(999L);
    }

    // TEST 7: TOTAL EXPENSES
    @Test
    void testTotalExpenses() {

        when(expenseRepository.getTotalExpenses())
                .thenReturn(1500.0);

        Double total = expenseService.getTotalExpenses();

        assertEquals(1500.0, total);

        verify(expenseRepository).getTotalExpenses();
    }
}
