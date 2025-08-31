package org.example.taskpilot_taskmanager.task.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChecklistItemCreateRequestDTO {
    @NotBlank
    private String label;

    private Boolean completed;
    private String description;

    private TimeEstimateDTO expectedTaskTime;
}
