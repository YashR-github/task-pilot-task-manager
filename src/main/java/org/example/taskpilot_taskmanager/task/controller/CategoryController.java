package org.example.taskpilot_taskmanager.task.controller;


import org.example.taskpilot_taskmanager.common.dtos.ResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.*;
import org.example.taskpilot_taskmanager.task.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

   //create api
    @PostMapping
    public ResponseEntity<ResponseDTO<CategoryCreateResponseDTO>> createCategory(@RequestBody CategoryCreateRequestDTO createRequestDTO){
        CategoryCreateResponseDTO categoryCreateResponseDTO = categoryService.createCategory(createRequestDTO.getName(), createRequestDTO.getCategoryCode(), createRequestDTO.getDescription(), createRequestDTO.getCategoryType()) ;
        ResponseDTO<CategoryCreateResponseDTO> responseDTO = new ResponseDTO<>("Category Created Successfully!", categoryCreateResponseDTO);
        return new ResponseEntity<>(responseDTO, org.springframework.http.HttpStatus.CREATED);
    }


    //delete api
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();

    }


    //update api
    @PatchMapping("{id}")
    public ResponseEntity<ResponseDTO<CategoryUpdateResponseDTO>> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryUpdateRequestDTO updateRequestDTO){
        CategoryUpdateResponseDTO categoryUpdateResponseDTO = categoryService.updateCategory(categoryId, updateRequestDTO.getName(), updateRequestDTO.getCategoryCode(), updateRequestDTO.getDescription(), updateRequestDTO.getCategoryType()) ;
        ResponseDTO<CategoryUpdateResponseDTO> responseDTO = new ResponseDTO<>("Category Updated Successfully!", categoryUpdateResponseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    //get all categories
    @GetMapping
    public ResponseEntity<ResponseDTO<List<CategoryResponseDTO>>> getAllCategories(){
        List<CategoryResponseDTO> categoryResponseDTOList = categoryService.getAllUserCategories();
        ResponseDTO<List<CategoryResponseDTO>> responseDTO = new ResponseDTO<>("All Categories Retrieved Successfully!", categoryResponseDTOList);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }




}
