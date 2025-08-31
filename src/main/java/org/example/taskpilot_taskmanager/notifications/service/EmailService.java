package org.example.taskpilot_taskmanager.notifications.service;


import org.example.taskpilot_taskmanager.notifications.dtos.EmailRequestDTO;

public interface EmailService {
    void send(EmailRequestDTO request) throws Exception;
}
