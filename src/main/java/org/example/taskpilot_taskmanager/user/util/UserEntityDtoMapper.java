package org.example.taskpilot_taskmanager.user.util;

import org.example.taskpilot_taskmanager.user.dtos.UserLoginResponseDTO;
import org.example.taskpilot_taskmanager.user.dtos.UserSignupResponseDTO;
import org.example.taskpilot_taskmanager.user.model.User;
import org.springframework.stereotype.Component;


@Component
public class UserEntityDtoMapper {


    public static UserSignupResponseDTO toUserSignupResponseDto(User user){
        UserSignupResponseDTO userSignupResponseDTO = new UserSignupResponseDTO();
        userSignupResponseDTO.setName(user.getName());
        userSignupResponseDTO.setUsername(user.getUsername());
        userSignupResponseDTO.setEmail(user.getEmail());
        userSignupResponseDTO.setUserRole(user.getUserRole().name());
        return userSignupResponseDTO;
    }

    public static UserLoginResponseDTO toUserLoginResponseDto(User user){
    UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
    if(user.getUsername() != null){
    userLoginResponseDTO.setUsername(user.getUsername()); }
    if(user.getEmail() != null){
    userLoginResponseDTO.setEmail(user.getEmail()); }
    userLoginResponseDTO.setUserRole(user.getUserRole().name());
    return userLoginResponseDTO;
    }
}
