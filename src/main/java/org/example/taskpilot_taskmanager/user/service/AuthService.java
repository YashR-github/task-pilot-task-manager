package org.example.taskpilot_taskmanager.user.service;

import org.example.taskpilot_taskmanager.user.dtos.AuthLoginResponseDTO;
import org.example.taskpilot_taskmanager.user.dtos.UserSignupResponseDTO;
import org.example.taskpilot_taskmanager.user.exceptions.UserAlreadyExistException;
import org.example.taskpilot_taskmanager.user.exceptions.UserNotFoundException;


public interface AuthService {

    public UserSignupResponseDTO signUp(String name, String username, String email, String password) throws UserAlreadyExistException;

    public AuthLoginResponseDTO login(String username, String email , String password) throws UserNotFoundException ;

}
