package org.example.taskpilot_taskmanager.task.exceptions;

public class DuplicateCategoryException extends RuntimeException {
    public DuplicateCategoryException(String message) {
        super(message);
    }
}
