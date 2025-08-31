package org.example.taskpilot_taskmanager.task.exceptions;

public class ChecklistItemAlreadyCompletedException extends RuntimeException {
    public ChecklistItemAlreadyCompletedException(String message) {
        super(message);
    }
}
