package org.example.taskpilot_taskmanager.task.exceptions;

public class InvalidTaskCompletedBeforeChecklistException extends RuntimeException {
    public InvalidTaskCompletedBeforeChecklistException(String message) {
        super(message);
    }
}
