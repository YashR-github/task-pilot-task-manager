package org.example.taskpilot_taskmanager.task.dtos;


import lombok.Data;
import org.example.taskpilot_taskmanager.task.enums.CategoryType;

@Data
public class CategoryResponseDTO {
    private String name;
    private String description;
    private String categoryCode;
    private CategoryType categoryType;
}
