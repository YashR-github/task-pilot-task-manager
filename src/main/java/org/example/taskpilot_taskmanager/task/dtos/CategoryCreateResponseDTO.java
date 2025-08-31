package org.example.taskpilot_taskmanager.task.dtos;


import lombok.Data;
import org.example.taskpilot_taskmanager.task.enums.CategoryType;

import java.time.LocalDateTime;

@Data
public class CategoryCreateResponseDTO {
    private LocalDateTime createdAt;
    private Long categoryId;
    private String name;
    private String description;
    private CategoryType categoryType;
    private String categoryCode;
}
