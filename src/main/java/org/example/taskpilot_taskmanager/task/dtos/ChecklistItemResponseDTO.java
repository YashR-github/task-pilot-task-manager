package org.example.taskpilot_taskmanager.task.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Data 
public class ChecklistItemResponseDTO {

    private String remarkMessage;
    private Long checklistId;

    private LocalDateTime timestamp;
    private String label;

    private Boolean completed;
    private String description;

    private TimeEstimateDTO expectedTaskTime;
}
