package org.example.taskpilot_taskmanager.task.dtos;


import lombok.Data;
import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;


@Data
public class TaskUpdateRequestDTO {

    private String name;
    private String description;
    private String categoryCode;
    private TaskPriority taskPriority;
    private TaskStatus status; // Todo should 'not allow' to change status directly from same update api, should have seperate api for updating status
    private TimeEstimateDTO expectedTaskTime;

}
