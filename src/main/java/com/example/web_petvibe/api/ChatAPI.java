package com.example.web_petvibe.api;



import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@SecurityRequirement(name = "api")
public class ChatAPI {

    @Autowired
    private com.example.web_petvibe.service.ChatService chatService;

    @PostMapping
    public String chat(@RequestBody String message) {
        return chatService.chat(message);
    }
}

