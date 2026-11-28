package com.smartspend.smartspend.controller;


import com.smartspend.smartspend.Service.AiService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    private final AiService aiService;
    public AiController(AiService aiService)
    {
        this.aiService=aiService;
    }

    @PostMapping("/test")
    public Map<String,String> testAi() {
        try {
            String response = aiService.generateResponse(
                    "Give me three simple money making tips for a collage student."
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
}
