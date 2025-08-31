package org.example.taskpilot_taskmanager.user.exceptions;



public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
