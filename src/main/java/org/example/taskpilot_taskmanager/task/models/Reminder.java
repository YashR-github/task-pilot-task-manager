package org.example.taskpilot_taskmanager.task.models;


import jakarta.persistence.Entity;
import org.example.taskpilot_taskmanager.common.model.Basemodel;

import java.time.LocalDateTime;

@Entity
public class Reminder extends Basemodel {
    private String inputReminderDescription;
    private LocalDateTime reminderDateTime;




}
