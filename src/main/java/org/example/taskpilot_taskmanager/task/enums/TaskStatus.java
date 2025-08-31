package org.example.taskpilot_taskmanager.task.enums;

public enum TaskStatus {
    NOT_STARTED,
    INPROGRESS, // might change the status to  inprogress when started on a task
    COMPLETED,

    //future features
    RESCHEDULED, // might help to show to user the date it is rescheduled to
    ONHOLD,  // might provide option to put task on hold, // might also allow reminder for this task to start
    PENDING_TASK  // All pending tasks that are not selected to do by user
}
