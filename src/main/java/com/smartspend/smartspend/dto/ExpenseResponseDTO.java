package com.smartspend.smartspend.dto;

import java.time.LocalDate;

public record ExpenseResponseDTO(
        Long id,
        Double amount,
        String category,
        String description,
        LocalDate date
) {}