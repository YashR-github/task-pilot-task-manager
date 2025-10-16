package org.example.taskpilot_taskmanager.task.controller;


import com.fasterxml.jackson.core.JsonProcessingException;

import org.example.taskpilot_taskmanager.task.service.ChatbotIntegrationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotIntegrationController {

    private final ChatbotIntegrationService aiService;

    ChatbotIntegrationController(ChatbotIntegrationService aiService) {
        this.aiService = aiService;
    }


    @PostMapping("/query")
    public String chatbotIntegration(@RequestBody String message) throws JsonProcessingException {
        String response = aiService.generateResponse(message);
        return response;
    }
}
