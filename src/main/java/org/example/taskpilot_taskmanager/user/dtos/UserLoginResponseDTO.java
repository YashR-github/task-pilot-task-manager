package org.example.taskpilot_taskmanager.user.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResponseDTO {
//    private String token;
    private String username;
    private String email;
    private String userRole;
}
