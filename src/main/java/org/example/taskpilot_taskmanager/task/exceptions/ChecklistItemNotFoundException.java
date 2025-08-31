package org.example.taskpilot_taskmanager.task.exceptions;

public class ChecklistItemNotFoundException extends RuntimeException {
    public ChecklistItemNotFoundException(String message) {
        super(message);
    }
}
