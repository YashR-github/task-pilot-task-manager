package org.example.taskpilot_taskmanager.task.dtos;

import lombok.Data;
import org.example.taskpilot_taskmanager.task.enums.CategoryType;

import java.time.LocalDateTime;


@Data
public class CategoryUpdateResponseDTO {

    private LocalDateTime updatedAt;
    private Long categoryId;
    private String name;
    private String description;
    private CategoryType categoryType;
    private String categoryCode;
}
