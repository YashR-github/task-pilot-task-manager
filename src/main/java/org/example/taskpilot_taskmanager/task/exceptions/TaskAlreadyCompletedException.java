package org.example.taskpilot_taskmanager.task.exceptions;

public class TaskAlreadyCompletedException extends RuntimeException {
    public TaskAlreadyCompletedException(String message) {
        super(message);
    }
}
