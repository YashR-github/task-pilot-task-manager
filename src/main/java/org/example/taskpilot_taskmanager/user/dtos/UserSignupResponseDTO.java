package org.example.taskpilot_taskmanager.user.dtos;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserSignupResponseDTO {

    private String name;

    private String username;

    private String email;

    private String userRole;
}
