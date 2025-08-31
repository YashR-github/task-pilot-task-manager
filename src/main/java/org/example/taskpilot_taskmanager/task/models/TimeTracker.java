package org.example.taskpilot_taskmanager.task.models;


import jakarta.persistence.Entity;
import org.example.taskpilot_taskmanager.common.model.Basemodel;

import java.time.Duration;

@Entity
public class TimeTracker extends Basemodel {

    private Duration duration;

}
