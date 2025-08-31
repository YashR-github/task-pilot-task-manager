package org.example.taskpilot_taskmanager.task.dtos;

import lombok.Data;


@Data
public class ChecklistItemUpdateRequestDTO {

    private String label;

    private Boolean completed;
    private String description;

    private TimeEstimateDTO expectedTaskTime;
}
