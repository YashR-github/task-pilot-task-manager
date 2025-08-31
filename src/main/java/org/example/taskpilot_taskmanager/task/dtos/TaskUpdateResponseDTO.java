package org.example.taskpilot_taskmanager.task.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL) //provides non null fields only in output
@Data
public class TaskUpdateResponseDTO {

    private String remarkMessage;
    private Long taskId;
    private LocalDateTime updatedAt;
    private String name;
    private String description;
    private String categoryCode; // no need to return full category as category is not updated using Task
    private TaskPriority taskPriority;
    private TaskStatus status;
    private TimeEstimateDTO expectedTaskTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration timeTillNow;
    private Duration completionTime;

}
