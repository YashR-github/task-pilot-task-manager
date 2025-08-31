package org.example.taskpilot_taskmanager.task.repositories;

import org.example.taskpilot_taskmanager.task.models.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog,Long> {
}
