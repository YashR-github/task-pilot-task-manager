package org.example.taskpilot_taskmanager.user.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDTO {

    private String email; //optional
    private String username; //optional

    @NotBlank(message="Password is required")
    private String password; //required

}
