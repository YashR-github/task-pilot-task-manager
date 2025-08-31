package org.example.taskpilot_taskmanager.task.dtos;



import lombok.Data;
import org.example.taskpilot_taskmanager.task.enums.CategoryType;

@Data
public class CategoryCreateRequestDTO {
    private String name;

    private String categoryCode;
    private String description;
    private CategoryType categoryType;

}
