package org.example.taskpilot_taskmanager.task.exceptions;

public class ChecklistItemAlreadyExistException extends RuntimeException {
    public ChecklistItemAlreadyExistException(String message) {
        super(message);
    }
}
