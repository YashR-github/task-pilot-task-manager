package org.example.taskpilot_taskmanager.task.dtos;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class TaskFilterRequestDTO {
    private String name;
    private String keyword;
    private String taskStatus;
    private String taskPriority;
    private String categoryName;
    private String categoryType;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;   // createdAfter and createdBefore provide search of tasks created within range
    private int page=0;
    private int size=5;
    private String sortBy="createdAt";
    private String sortDirection="asc";

}
