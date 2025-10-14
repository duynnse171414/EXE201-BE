package com.example.web_petvibe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${openrouter.api.key}")
    private String apiKey;

    public ChatService() {
        this.client = new OkHttpClient();
    }

    public String chat(String message) {
        try {
            String url = "https://openrouter.ai/api/v1/chat/completions";

            // JSON body
            String jsonBody = mapper.writeValueAsString(Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", List.of(Map.of("role", "user", "content", message))
            ));

            // Tạo request với Header đúng chuẩn OpenRouter
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("HTTP-Referer", "https://yourwebsite.com")
                    .header("X-Title", "PetVibe Chatbot")
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return "❌ Request failed: " + response.code() + " - " + response.message();
            }

            String responseBody = response.body().string();

            // Parse JSON trả về
            Map<?, ?> data = mapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) data.get("choices");

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                return (String) messageObj.get("content");
            }

            return "❌ No response from model.";

        } catch (IOException e) {
            e.printStackTrace();
            return "❌ Chat API error: " + e.getMessage();
        }
    }
}
