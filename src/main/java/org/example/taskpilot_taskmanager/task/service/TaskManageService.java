package org.example.taskpilot_taskmanager.task.service;

import org.example.taskpilot_taskmanager.task.dtos.*;
import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskManageService {

    //create task
    public TaskCreateResponseDTO createTask(String name, String description, String categoryCode, TaskPriority tasksPriority, TaskStatus taskStatus, TimeEstimateDTO timeEstimation);



   //update task
    public TaskUpdateResponseDTO updateTaskById(Long taskId, String name,String description, String categoryCode, TaskPriority taskPriority, TaskStatus taskStatus, TimeEstimateDTO expectedTaskTime);


    //get all tasks
    public List<TaskResponseDTO> getAllTasks();

    //get all tasks in paginated fashion
    public Page<TaskResponseDTO> getPaginatedTasks(int page, int size, String sortBy, String sortDirection);

    // get filtered+sorted+search query enabled+ paginated tasks
   public Page<TaskResponseDTO> getFilteredTasks(TaskFilterRequestDTO taskFilterRequestDTO);

   //get single task by id
   public TaskResponseDTO getSingleTaskByID(Long id);

    // delete task
   public void deleteTask(Long taskId);


// extra features below:
    public TaskUpdateResponseDTO markTaskCompleted(Long taskId);

    public TaskUpdateResponseDTO logTaskStart(Long taskId);


}
