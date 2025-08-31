package org.example.taskpilot_taskmanager.task.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.taskpilot_taskmanager.common.model.Basemodel;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChecklistItem extends Basemodel {

    private String checklistRemark;
    private String label;

    @Column(nullable=false)
    private boolean completed= false;
    private String description;

    private Long completionRank;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    @JsonBackReference
    private Task task;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Duration totalTimeTaken;

    @Embedded
    private TimeEstimation expectedTaskTime;

    //initialize embedded objects
    @PostLoad
    @PrePersist
    @PreUpdate
    private void initializeEmbeddedObjects() {
        if (expectedTaskTime == null) {
            expectedTaskTime = new TimeEstimation();
        }
    }

}
