package org.example.taskpilot_taskmanager.task.service;


import jakarta.transaction.Transactional;
import org.example.taskpilot_taskmanager.task.dtos.CategoryCreateResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.CategoryResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.CategoryUpdateResponseDTO;
import org.example.taskpilot_taskmanager.task.enums.CategoryType;
import org.example.taskpilot_taskmanager.task.exceptions.CategoryNotFoundException;
import org.example.taskpilot_taskmanager.task.exceptions.DuplicateCategoryException;
import org.example.taskpilot_taskmanager.task.models.Category;
import org.example.taskpilot_taskmanager.task.repositories.CategoryRepository;
import org.example.taskpilot_taskmanager.task.utils.TaskEntityDTOMapper;
import org.example.taskpilot_taskmanager.user.model.User;
import org.example.taskpilot_taskmanager.user.util.AuthenticatedUserUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    public CategoryServiceImpl(CategoryRepository categoryRepository, AuthenticatedUserUtil authenticatedUserUtil) {
        this.categoryRepository = categoryRepository;
        this.authenticatedUserUtil = authenticatedUserUtil;
    }


    //create
    @Transactional
    public CategoryCreateResponseDTO createCategory(String name, String categoryCode, String description, CategoryType categoryType){
        //get authenticated user
        User user = authenticatedUserUtil.getCurrentUser();
        //check uniqueness
        Optional<Category> optionalCategory = categoryRepository.findByUserAndCode(user,categoryCode);
        if(optionalCategory.isPresent()){
            throw new DuplicateCategoryException("Category with code: \""+categoryCode+"\" already exist for the user .Please change codes to avoid duplicate entries.");
        }
        Optional<Category> optionalCategory2 = categoryRepository.findByNameAndCategoryType(name, categoryType);
        if(optionalCategory2.isPresent()){
            throw new DuplicateCategoryException("Category with name: \""+name+"\" and category type: \"" +categoryType+ "\"already exist for the user.");
        }

        Category category = new Category();

        category.setName(name);
        category.setCode(categoryCode);
        category.setDescription(description);
        category.setCategoryType(categoryType);

        return TaskEntityDTOMapper.convertCategoryToCategoryCreateResponseDTO(category);

    }



    // update
    @Transactional
    public CategoryUpdateResponseDTO updateCategory(Long categoryId, String name, String categoryCode, String description, CategoryType categoryType){
        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();
        Optional<Category> optionalCategory = categoryRepository.findByUserAndId(user,categoryId);
        if(optionalCategory.isEmpty()) {
        throw new CategoryNotFoundException("Category not found with given id: "+categoryId);
        }
        Category category= optionalCategory.get();
        if(name!=null && !name.isEmpty()){
            if(categoryType!=null) {
                Optional <Category> optionalCategory2 = categoryRepository.findByNameAndCategoryType(name, categoryType);
                if(optionalCategory2.isPresent()){
                    throw new DuplicateCategoryException("Category with name: \""+name+"\" and category type: \"" +categoryType+ "\"already exist for the user.");
                }
            }
            category.setName(name);
        }
        if(categoryCode!=null && !categoryCode.isEmpty()){
            Optional<Category> optionalCategory2 = categoryRepository.findByUserAndCode(user,categoryCode);
            if(optionalCategory2.isPresent()){
                throw new DuplicateCategoryException("Category with code: \""+categoryCode+"\" already exist for the user .Please change codes to avoid duplicate entries.");
            }
            category.setCode(categoryCode);
        }
        if(description!=null && !description.isEmpty()){
            category.setDescription(description);
        }
        if(categoryType!=null) {
            category.setCategoryType(categoryType);
        }

        return TaskEntityDTOMapper.convertCategoryToCategoryUpdateResponseDTO(categoryRepository.save(category));
    }



    //delete
    public void deleteCategory(Long categoryId){
        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();
        Optional<Category> optionalCategory = categoryRepository.findByUserAndId(user,categoryId);

        if(optionalCategory.isEmpty()) {
        throw new CategoryNotFoundException("Category not found with given id: "+categoryId);
        }
        categoryRepository.delete(optionalCategory.get());
    }


    //get all categories
    public List<CategoryResponseDTO> getAllUserCategories(){
        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();
        List<Category> categories = categoryRepository.findAllByUser(user);
        List<CategoryResponseDTO> categoryDTOList = new ArrayList<>();
        for(Category category : categories){
            categoryDTOList.add(TaskEntityDTOMapper.convertCategoryToCategoryResponseDTO(category));
        }
        return categoryDTOList;
    }


}
