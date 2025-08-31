package org.example.taskpilot_taskmanager.task.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;


@Getter
@Setter
public class TaskCreateRequestDTO {

    @NotBlank
    private String name;
    private String description;
    private String categoryCode;

    private TaskPriority taskPriority;

    @Column(nullable=false) //Todo 'dont allow' setting task status from create api, default for create should be NOT_STARTED only
    private TaskStatus status = TaskStatus.NOT_STARTED;
    private TimeEstimateDTO expectedTaskTime;

}
