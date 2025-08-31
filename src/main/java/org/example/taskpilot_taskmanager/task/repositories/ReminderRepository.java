package org.example.taskpilot_taskmanager.task.repositories;

import org.example.taskpilot_taskmanager.task.models.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReminderRepository extends JpaRepository<Reminder,Long> {
}


