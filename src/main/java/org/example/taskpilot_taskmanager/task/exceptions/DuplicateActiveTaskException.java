package org.example.taskpilot_taskmanager.task.exceptions;

public class DuplicateActiveTaskException extends RuntimeException {
    public DuplicateActiveTaskException(String message) {
        super(message);
    }
}
