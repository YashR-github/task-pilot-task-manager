package org.example.taskpilot_taskmanager.task.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.example.taskpilot_taskmanager.common.model.Basemodel;


// would check later how to acccomplish this feature of recurring
@Entity
public class RecurringTask extends Basemodel {
    @ManyToOne
    @JoinColumn(name = "recurring_task_id") // just a custom foreign key name to override jpa naming,this won't hinder jpa in any way.
    private Task recurringTask;





    public Task getRecurringTask() {
        return recurringTask;
    }

    public void setRecurringTask(Task recurringTask) {
        this.recurringTask = recurringTask;
    }
}
