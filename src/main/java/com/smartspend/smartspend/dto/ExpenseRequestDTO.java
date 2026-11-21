package com.smartspend.smartspend.dto;


import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ExpenseRequestDTO(

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        Double amount,

        @NotBlank(message = "Category cannot be empty")
        String category,

        @NotBlank(message = "Description cannot be empty")
        @Size(max = 255)
        String description,

        @NotNull(message = "Date is required")
        LocalDate date

) {}