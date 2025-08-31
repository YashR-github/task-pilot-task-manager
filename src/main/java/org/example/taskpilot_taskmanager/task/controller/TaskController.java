package org.example.taskpilot_taskmanager.task.controller;

import jakarta.validation.Valid;
import org.example.taskpilot_taskmanager.common.dtos.ResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.*;
import org.example.taskpilot_taskmanager.task.service.TaskManageService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping(value="/tasks")
public class TaskController {


    private TaskManageService taskManageService;

    @Autowired
    public TaskController(TaskManageService taskManageService) {
        this.taskManageService = taskManageService;
    }



// Usually Theres no need for url other than base url for post,patch and get all api, since the right http call would be enough
// to hit the right api via the base url "/tasks".



// After each login, I should pass the token in api auth header so as to check for each user
    @PostMapping
    public ResponseEntity<ResponseDTO<TaskCreateResponseDTO>> createTask(@RequestBody TaskCreateRequestDTO taskCreateRequestDTO){
        TaskCreateResponseDTO taskCreateResponseDto = taskManageService.createTask(taskCreateRequestDTO.getName(), taskCreateRequestDTO.getDescription(), taskCreateRequestDTO.getCategoryCode(), taskCreateRequestDTO.getTaskPriority(), taskCreateRequestDTO.getStatus(), taskCreateRequestDTO.getExpectedTaskTime());
        ResponseDTO<TaskCreateResponseDTO> responseDto = new ResponseDTO("Task Created Successfully!", taskCreateResponseDto);

       return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }



    @PatchMapping("/{taskId}") // PATCH can update one or more fields as per json body
    public ResponseEntity<ResponseDTO<TaskUpdateResponseDTO>> updateTaskById(@PathVariable Long taskId, @RequestBody @Valid TaskUpdateRequestDTO request){
        TaskUpdateResponseDTO taskUpdateResponseDTO = taskManageService.updateTaskById(taskId, request.getName(),request.getDescription(),request.getCategoryCode(), request.getTaskPriority(),request.getStatus(), request.getExpectedTaskTime());
        ResponseDTO<TaskUpdateResponseDTO> responseDTO = new ResponseDTO<>("Updated Task Successfully!", taskUpdateResponseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }



     // 1. get all tasks without pagination
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO<List<TaskResponseDTO>>> getAllTasks(){
        List<TaskResponseDTO> taskResponseDTOS = taskManageService.getAllTasks();
        ResponseDTO<List<TaskResponseDTO>> responseDTO= new ResponseDTO<>("All Tasks Retrieved Successfully!", taskResponseDTOS);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }



    ///  2.get paginated tasks
    @GetMapping("/paginated")
    public ResponseEntity<ResponseDTO<Page<TaskResponseDTO>>> getPaginatedTasks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "asc") String sortDirection){
        Page<TaskResponseDTO> paginatedTaskResponseDTOs= taskManageService.getPaginatedTasks(page, size, sortBy, sortDirection);
        ResponseDTO<Page<TaskResponseDTO>> responseDTO= new ResponseDTO<>("Paginated Tasks Retrieved Successfully!", paginatedTaskResponseDTOs);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    /// 3. Final - updated getALL (filter+ sort+ search+ pagination)
    @GetMapping
    public ResponseEntity<ResponseDTO<Page<TaskResponseDTO>>> getFilteredTasks(@ParameterObject TaskFilterRequestDTO taskFilterRequestDTO ){
    Page<TaskResponseDTO> filteredTasks = taskManageService.getFilteredTasks(taskFilterRequestDTO);
    ResponseDTO<Page<TaskResponseDTO>> responseDTO= new ResponseDTO<>("Filtered Tasks Retrieved Successfully!", filteredTasks);

    return ResponseEntity.ok(responseDTO);

    }




    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseDTO<TaskResponseDTO>> getSingleTaskByID(@PathVariable Long taskId){
        TaskResponseDTO taskResponseDTO = taskManageService.getSingleTaskByID(taskId);
        ResponseDTO<TaskResponseDTO> responseDTO = new ResponseDTO<>("Task Retrieved Successfully!", taskResponseDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }


    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId){
        taskManageService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }




/// -----------------------  additional task logging  features ------------------------------------------------------


    @PatchMapping("/{taskId}/mark-task-complete")  // changes status of task
    public ResponseEntity<ResponseDTO<TaskUpdateResponseDTO>>  markTaskCompleted(@PathVariable Long taskId){
      TaskUpdateResponseDTO taskUpdateResponseDTO= taskManageService.markTaskCompleted(taskId);
      ResponseDTO<TaskUpdateResponseDTO> responseDTO = new ResponseDTO<>("Task along with all corresponding checklist items Marked as Complete.",taskUpdateResponseDTO);
      return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }


    @PatchMapping("/{taskId}/log-start")
    public ResponseEntity<ResponseDTO<TaskUpdateResponseDTO>> startTask(@PathVariable Long taskId) {
        TaskUpdateResponseDTO taskUpdateResponseDTO= taskManageService.logTaskStart(taskId);
        ResponseDTO<TaskUpdateResponseDTO> responseDTO = new ResponseDTO<>("Task marked as Started !", taskUpdateResponseDTO);
        return new ResponseEntity<>(responseDTO,HttpStatus.OK);
    }

//    @PatchMapping("/mark-all-completed")
//    public ResponseEntity<ResponseDTO<TaskDTO>> markAllCompleted(@PathVariable Long id){
//
//    }
//
//    // GET flitered list of tasks based on status eg: Pending, On_Hold- Already Covered in getFilteredTask api
//    @GetMapping(value="status/{status}")
//    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getTasksByStatus(@PathVariable String status){
//
//    }




}
