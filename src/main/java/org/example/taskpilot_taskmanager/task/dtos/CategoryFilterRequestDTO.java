package org.example.taskpilot_taskmanager.task.dtos;


import lombok.Data;


@Data
public class CategoryFilterRequestDTO {
    private String keyword;
    private String name;   //exact match
    private String description; //partial match
    private String type;
    private String code;
    private int page=0 ;
    private int size=10;
    private String sortBy ="createdAt";
    private String sortDirection = "desc" ;

}
