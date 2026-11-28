package com.smartspend.smartspend.controller;


import com.smartspend.smartspend.Service.AiService;
import com.smartspend.smartspend.Service.ExpenseAnalysisService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    private final ExpenseAnalysisService
            expenseAnalysisService;

    public AiController(
            AiService aiService,
            ExpenseAnalysisService expenseAnalysisService) {

        this.aiService = aiService;
        this.expenseAnalysisService =
                expenseAnalysisService;
    }

    @PostMapping("/test")
    public Map<String, String> testAi() {

        try {

            String response = aiService.generateResponse(
                    "Give me three simple money-saving tips."
            );

            return Map.of("response", response);

        } catch (RestClientException ex) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Unable to communicate with AI provider"
            );

        } catch (IllegalStateException ex) {

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    ex.getMessage()
            );
        }
    }

    @PostMapping("/analyze")
    public Map<String, Object> analyzeExpenses(
            @RequestParam(required = false) String month) {

        try {

            YearMonth selectedMonth = month == null
                    ? YearMonth.now(
                    ZoneId.of("Asia/Kolkata"))
                    : YearMonth.parse(month);

            return expenseAnalysisService
                    .analyzeExpenses(selectedMonth);

        } catch (DateTimeParseException ex) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Month must be in YYYY-MM format"
            );

        } catch (RestClientException ex) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Unable to communicate with AI provider"
            );

        } catch (IllegalStateException ex) {

            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    ex.getMessage()
            );
        }
    }
}