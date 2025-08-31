package org.example.taskpilot_taskmanager.user.exceptions;



public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
