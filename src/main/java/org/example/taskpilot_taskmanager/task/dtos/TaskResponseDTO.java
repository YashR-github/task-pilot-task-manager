package org.example.taskpilot_taskmanager.task.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL) //provides non null fields only in output
@Data
public class TaskResponseDTO {

    private String remarkMessage;
    private Long taskId;
    private LocalDateTime timestamp;
    private String name;
    private String description;
    private String categoryCode;
    private TaskPriority taskPriority;
    private TaskStatus taskStatus;
    private TimeEstimateDTO expectedTaskTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration completionTime;
    private Duration timeTillNow;

}
