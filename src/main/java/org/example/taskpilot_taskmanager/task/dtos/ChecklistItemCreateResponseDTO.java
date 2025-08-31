package org.example.taskpilot_taskmanager.task.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChecklistItemCreateResponseDTO {

    private Long checklistId;
    private String label;
    private Boolean completed;
    private String description;
    private LocalDateTime createdAt;

    private TimeEstimateDTO expectedTaskTime;
}
