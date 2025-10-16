package org.example.taskpilot_taskmanager.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ChatbotIntegrationService {

    public String generateResponse(String prompt) throws JsonProcessingException;

}
