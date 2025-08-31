package org.example.taskpilot_taskmanager.user.exceptions;



public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
