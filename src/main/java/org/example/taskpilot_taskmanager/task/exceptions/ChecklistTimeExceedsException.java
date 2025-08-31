package org.example.taskpilot_taskmanager.task.exceptions;

public class ChecklistTimeExceedsException extends RuntimeException {
    public ChecklistTimeExceedsException(String message) {
        super(message);
    }
}
