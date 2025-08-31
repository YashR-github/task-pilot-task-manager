package org.example.taskpilot_taskmanager.task.models;

import jakarta.persistence.*;
import org.example.taskpilot_taskmanager.common.model.Basemodel;
import org.example.taskpilot_taskmanager.task.enums.CategoryType;
import org.example.taskpilot_taskmanager.user.model.User;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uk_category_user_code",
                columnNames = {"user_id", "code"}
        )
)
public class Category extends Basemodel {

    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    @Column(nullable= false)
    private String code;  //TODO still check uniqueness at service level

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType= CategoryType.NOT_SET;


    @PrePersist
    public void prePersist(){
        if(description==null || description.isBlank())
            description= name;
    }




    //getters and setters

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }
}
