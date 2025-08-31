package org.example.taskpilot_taskmanager.task.exceptions;

public class TaskAlreadyStartedException extends RuntimeException {
    public TaskAlreadyStartedException(String message) {
        super(message);
    }
}
