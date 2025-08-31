package org.example.taskpilot_taskmanager.task.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChecklistItemUpdateResponseDTO {

    private String remarkMessage;
    private Long checklistId;

    private String label;

    private Boolean completed;
    private String description;
    private LocalDateTime updatedAt;

    private TimeEstimateDTO expectedTaskTime;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration completionTime;
}
