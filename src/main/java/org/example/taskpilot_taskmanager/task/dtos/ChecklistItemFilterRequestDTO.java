package org.example.taskpilot_taskmanager.task.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChecklistItemFilterRequestDTO {
    private String keyword;
    private String label;    // exact match
    private String description; //partial match
    private Boolean completed;
    private Long taskId;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore ;
    private int page=0;
    private int size=10;
    private String sortBy="createdAt";
    private String sortDirection="desc";

}
