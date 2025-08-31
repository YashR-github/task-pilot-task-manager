package org.example.taskpilot_taskmanager.task.repositories;

import org.example.taskpilot_taskmanager.task.models.ChecklistItem;
import org.example.taskpilot_taskmanager.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem,Long> {

    Optional<ChecklistItem> findByTask_IdAndLabelAndTask_User(Long taskId, String checklistItemLabel,User user);

    List<ChecklistItem> findAllByTaskIdAndTask_User(Long taskId,User user);


    Optional<ChecklistItem> findByIdAndTask_UserAndTask_Id(Long checklistId, User user, Long taskId);

    List<ChecklistItem> findAllByTask_IdAndTask_UserAndCompleted(Long taskId, User user, boolean completed);
}
