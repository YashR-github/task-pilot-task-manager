package org.example.taskpilot_taskmanager.task.dtos;

import lombok.Getter;
import lombok.Setter;
import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskCreateResponseDTO {

    private Long taskId;
    private LocalDateTime createdAt;
    private String name;
    private String description;
    private CategoryResponseDTO category;
    private TaskPriority taskPriority;
    private TaskStatus status;
    private TimeEstimateDTO expectedTaskTime;
}
