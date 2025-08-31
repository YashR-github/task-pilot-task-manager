package org.example.taskpilot_taskmanager.task.models;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.example.taskpilot_taskmanager.common.model.Basemodel;
import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;
import org.example.taskpilot_taskmanager.user.model.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
public class Task extends Basemodel {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    private String taskRemark="";

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;


    @Enumerated(EnumType.STRING) // saves field as enum string in db
    private TaskPriority taskPriority;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus= TaskStatus.NOT_STARTED;


    //Todo: For future -Recurrence
//    private boolean isRecurringTask;
//    private RecurrenceType recurrenceType; // 'Weekly', 'Daily', 'Monthly'
//    private Date recurrenceTill;  // Till when recurrence exist


    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Duration totalTimeTaken;
    private Duration checklistItemsEstimateDuration= Duration.ZERO;

    private Duration timeTakenTillNow = Duration.ZERO;
    @Embedded
    private TimeEstimation expectedTaskTime; // calculated as startTime+ expectedTaskTime duration

    @OneToMany (mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reschedule> reschedules;



    //--------------------- helper methods for updating checklistItemsEstimateDuration field --------------------------
    public void addChecklistEstimate(Duration toAdd){
        this.checklistItemsEstimateDuration = this.checklistItemsEstimateDuration.plus(toAdd);
    }

    public void subtractChecklistEstimate(Duration toSubtract){
        this.checklistItemsEstimateDuration = this.checklistItemsEstimateDuration.minus(toSubtract);
    }



//initialize embedded objects
    @PostLoad
    @PrePersist
    @PreUpdate
    private void initializeEmbeddedObjects() {
        if (expectedTaskTime == null) {
            expectedTaskTime = new TimeEstimation();
        }
    }


//  custom getter
    public Duration getTimeTakenTillNow() {
        if (startDateTime== null) return null;
        timeTakenTillNow = Duration.between(startDateTime, LocalDateTime.now());
        return timeTakenTillNow;
    }

}
