package org.example.taskpilot_taskmanager.task.exceptions;

public class ChecklistItemAlreadyStartedException extends RuntimeException {
    public ChecklistItemAlreadyStartedException(String message) {
        super(message);
    }
}
