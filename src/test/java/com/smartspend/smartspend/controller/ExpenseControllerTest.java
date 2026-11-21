
package com.smartspend.smartspend.controller;

import com.smartspend.smartspend.dto.ExpenseRequestDTO;
import com.smartspend.smartspend.dto.ExpenseResponseDTO;
import com.smartspend.smartspend.exception.ExpenseNotFoundException;
import com.smartspend.smartspend.exception.GlobalExceptionHandler;
import com.smartspend.smartspend.Service.ExpenseService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExpenseController.class)
@Import(GlobalExceptionHandler.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    // TEST 1: GET EXPENSE BY ID
    @Test
    void shouldGetExpenseById() throws Exception {

        ExpenseResponseDTO expense = new ExpenseResponseDTO(
                1L,
                500.0,
                "Food",
                "Lunch",
                LocalDate.of(2026, 11, 21)
        );

        when(expenseService.getExpenseById(1L))
                .thenReturn(expense);

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(500.0))
                .andExpect(jsonPath("$.category").value("Food"));

        verify(expenseService).getExpenseById(1L);
    }

    // TEST 2: POST VALID EXPENSE
    @Test
    void shouldCreateExpense() throws Exception {

        ExpenseResponseDTO response = new ExpenseResponseDTO(
                1L,
                750.0,
                "Shopping",
                "Bought a shirt",
                LocalDate.of(2026, 11, 21)
        );

        when(expenseService.addExpense(any(ExpenseRequestDTO.class)))
                .thenReturn(response);

        String requestJson = """
                {
                    "amount": 750,
                    "category": "Shopping",
                    "description": "Bought a shirt",
                    "date": "2026-11-21"
                }
                """;

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(750.0))
                .andExpect(jsonPath("$.category").value("Shopping"));

        verify(expenseService).addExpense(any(ExpenseRequestDTO.class));
    }

    // TEST 3: POST INVALID EXPENSE
    @Test
    void shouldRejectInvalidExpense() throws Exception {

        String invalidJson = """
                {
                    "amount": -500,
                    "category": "",
                    "description": "",
                    "date": null
                }
                """;

        mockMvc.perform(post("/api/expenses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amount").exists())
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.date").exists());

        verifyNoInteractions(expenseService);
    }

    // TEST 4: EXPENSE NOT FOUND
    @Test
    void shouldReturn404WhenExpenseNotFound() throws Exception {

        when(expenseService.getExpenseById(999L))
                .thenThrow(new ExpenseNotFoundException(
                        "Expense not found with ID: 999"
                ));

        mockMvc.perform(get("/api/expenses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Expense not found with ID: 999"));
    }
}
