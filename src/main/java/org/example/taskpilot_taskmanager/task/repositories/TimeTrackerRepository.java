package org.example.taskpilot_taskmanager.task.repositories;

import org.example.taskpilot_taskmanager.task.models.TimeTracker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTrackerRepository extends JpaRepository<TimeTracker,Long> {
}
