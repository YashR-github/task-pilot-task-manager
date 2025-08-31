package org.example.taskpilot_taskmanager.task.repositories;

import org.example.taskpilot_taskmanager.task.enums.CategoryType;
import org.example.taskpilot_taskmanager.task.models.Category;
import org.example.taskpilot_taskmanager.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface  CategoryRepository extends JpaRepository<Category,Long> {

   Optional<Category> findByCode(String code);

   Optional<Category> findByUserAndCode(User user, String categoryCode);

   Optional<Category> findByUserAndId(User user,Long categoryId);

   Optional<Category> findByNameAndCategoryType(String name, CategoryType categoryType);

   List<Category> findAllByUser(User user);

}
