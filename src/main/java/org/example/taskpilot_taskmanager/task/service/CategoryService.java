package org.example.taskpilot_taskmanager.task.service;


import org.example.taskpilot_taskmanager.task.dtos.CategoryCreateResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.CategoryResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.CategoryUpdateResponseDTO;
import org.example.taskpilot_taskmanager.task.enums.CategoryType;

import java.util.List;


public interface CategoryService {

    public CategoryCreateResponseDTO createCategory(String name, String categoryCode, String description, CategoryType categoryType) ;

    public void deleteCategory(Long categoryId) ;

    public CategoryUpdateResponseDTO updateCategory(Long categoryId, String name, String categoryCode, String description, CategoryType categoryType);

    public List<CategoryResponseDTO> getAllUserCategories();
}
