package org.example.taskpilot_taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing // Tells spring to enable automatic population of fields in classes annotated with @EntityListeners(AuditingEntityListener.class)
@EnableScheduling
public class TaskPilot_TaskManager {

    public static void main(String[] args) {
        SpringApplication.run(TaskPilot_TaskManager.class, args);
    }

}
