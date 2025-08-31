package org.example.taskpilot_taskmanager.user.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.taskpilot_taskmanager.task.models.Category;
import org.example.taskpilot_taskmanager.task.models.Task;
import org.example.taskpilot_taskmanager.user.enums.UserRole;
import org.example.taskpilot_taskmanager.user.enums.UserStatus;
import org.example.taskpilot_taskmanager.common.model.Basemodel;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users") // safe to rename in case using H2 db
@Data
public class User extends Basemodel {
    private String name;

    //must be unique username
    private String username;
    private String email;
    private String password;

    //optional add phone number, get reminder on phone no.
    @Enumerated(EnumType.STRING)
    private UserRole userRole; // Optional can create system that ADMIN can view all subordinate's task completion status
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

}
