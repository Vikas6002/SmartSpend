package com.smartspend.smartspend.Service;

import com.smartspend.smartspend.entity.Expense;
import com.smartspend.smartspend.Repository.ExpenseRepository;
import com.smartspend.smartspend.dto.ExpenseResponseDTO;
import com.smartspend.smartspend.exception.ExpenseNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void shouldGetExpenseById() {

        Expense expense = new Expense();

        expense.setId(1L);
        expense.setAmount(500);
        expense.setCategory("Food");
        expense.setDescription("Lunch");
        expense.setDate(LocalDate.of(2026, 11, 21));

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

    @Test
    void shouldThrowExceptionWhenExpenseNotFound() {

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
}