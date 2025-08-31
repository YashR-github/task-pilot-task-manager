package org.example.taskpilot_taskmanager.task.repositories;

import org.example.taskpilot_taskmanager.task.models.Reschedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RescheduleRepository extends JpaRepository<Reschedule,Long> {
}
