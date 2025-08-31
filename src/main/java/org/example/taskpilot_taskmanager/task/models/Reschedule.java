package org.example.taskpilot_taskmanager.task.models;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.example.taskpilot_taskmanager.common.model.Basemodel;

@Entity
@Data
public class Reschedule extends Basemodel {

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="task_id")
    private Task task;


    //TODO check whether this class Reschedule is needed , since Task and checklist already has update for time estimation
    private TimeEstimation expectedTaskTime; // TODO to add it in TaskUpdateCreateRequest and ResponseDTO
}
