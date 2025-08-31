package org.example.taskpilot_taskmanager.task.repositories;


import org.example.taskpilot_taskmanager.task.models.RecurringTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecurringTaskRepository extends JpaRepository<RecurringTask,Long> {



}
