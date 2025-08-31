package org.example.taskpilot_taskmanager.task.repositories;

import org.example.taskpilot_taskmanager.task.enums.TaskStatus;
import org.example.taskpilot_taskmanager.task.models.Task;
import org.example.taskpilot_taskmanager.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {

//    Optional<Task> findById(Long taskId); // maybe for admin, but still would require user to check by id

    Page<Task> findAllByUser(User user, Pageable pageable);
    List<Task> findAllByUser(User user);

    Optional<Task> findByIdAndUser(Long taskId, User user); //for delete task api

    Optional<Task> findByUserAndNameAndTaskStatusNot(User user, String name, TaskStatus taskStatus);

    List<Task> findByUserAndTaskStatus(User user, TaskStatus status);

    List<Task> findByUserAndTaskStatusNotIn(User user, List<TaskStatus> excludedStatuses);
}
