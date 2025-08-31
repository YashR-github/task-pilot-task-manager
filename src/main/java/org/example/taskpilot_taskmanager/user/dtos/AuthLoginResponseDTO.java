package org.example.taskpilot_taskmanager.user.dtos;


import lombok.Data;

@Data
public class AuthLoginResponseDTO {
    private String token;
    private UserLoginResponseDTO userLoginResponseDto;

    public AuthLoginResponseDTO(String token, UserLoginResponseDTO userLoginResponseDto) {
        this.token = token;
        this.userLoginResponseDto = userLoginResponseDto;
    }
}
