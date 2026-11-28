
package com.smartspend.smartspend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final RestClient restClient;

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.model}")
    private String model;

    public AiService(@Value("${groq.api.url}") String apiUrl) {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(10000);
        factory.setReadTimeout(60000);

        this.restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .requestFactory(factory)
                .build();
    }

    public String generateResponse(String prompt) {

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Groq API key is not configured"
            );
        }

        Map<String, Object> requestBody = Map.of(

                "model", model,

                "messages", List.of(

                        Map.of(
                                "role", "system",
                                "content",
                                "You are SmartSpend, a helpful expense tracking assistant."
                        ),

                        Map.of(
                                "role", "user",
                                "content", prompt
                        )

                ),

                "temperature", 0.5,

                "max_completion_tokens", 1024
        );

        Map<?, ?> response = restClient.post()

                .uri("/chat/completions")

                .headers(headers ->
                        headers.setBearerAuth(apiKey))

                .contentType(MediaType.APPLICATION_JSON)

                .body(requestBody)

                .retrieve()

                .body(Map.class);

        if (response != null
                && response.get("choices") instanceof List<?> choices
                && !choices.isEmpty()
                && choices.get(0) instanceof Map<?, ?> choice
                && choice.get("message") instanceof Map<?, ?> message
                && message.get("content") instanceof String content
                && !content.isBlank()) {

            return content;
        }

        throw new IllegalStateException(
                "No response received from AI"
        );
    }
}