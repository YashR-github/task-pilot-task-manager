package org.example.taskpilot_taskmanager.task.controller;


import jakarta.validation.Valid;
import org.example.taskpilot_taskmanager.common.dtos.ResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemCreateRequestDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemCreateResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemUpdateResponseDTO;
import org.example.taskpilot_taskmanager.task.service.ChecklistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task/{taskId}/checklist-items")
public class ChecklistItemController {

  private ChecklistItemService checklistItemService;

  @Autowired
  public ChecklistItemController(ChecklistItemService checklistItemService) {
    this.checklistItemService = checklistItemService;
  }


    //Possible apis
    //  1) Get all checklist items for a task : Output can include Task name, expected completion time and checklist item list

    // 2) Log start of checklist item

    // 3) Delete a checklist item

    // 4) Patch a checklist item

    //5) Add new checklist item in task
   @PostMapping
   public ResponseEntity<ResponseDTO<List<ChecklistItemCreateResponseDTO>>> createChecklistItemsForTask(@PathVariable Long taskId, @RequestBody @Valid List<ChecklistItemCreateRequestDTO>  checklistItemsCreateRequest){
    List<ChecklistItemCreateResponseDTO> taskChecklistCreateResponseDTO = checklistItemService.createChecklistItemsForTaskID(taskId, checklistItemsCreateRequest);

   ResponseDTO<List<ChecklistItemCreateResponseDTO>> responseDTO = new ResponseDTO<>("Checklist Items Created Successfully For the Task!", taskChecklistCreateResponseDTO);
   return new ResponseEntity<>(responseDTO, org.springframework.http.HttpStatus.CREATED);

  }


  @GetMapping("/{checklistId}")
  public ResponseEntity<ResponseDTO<ChecklistItemResponseDTO>> getSingleChecklistItemByTaskIDAndChecklistID(@PathVariable Long taskId, @PathVariable Long checklistId){
    ChecklistItemResponseDTO checklistItemResponseDTO = checklistItemService.getSingleChecklistItemByChecklistID(taskId, checklistId);
    checklistItemResponseDTO.setTimestamp(LocalDateTime.now());
    ResponseDTO<ChecklistItemResponseDTO> responseDTO = new ResponseDTO<>("Checklist Item Retrieved Successfully!", checklistItemResponseDTO);
    return new ResponseEntity<>(responseDTO, org.springframework.http.HttpStatus.OK);
  }

//
//  @GetMapping
//  public ResponseEntity<ResponseDTO<List<ChecklistItemResponseDTO>> getAllChecklistItemsByTaskID(@PathVariable Long taskId){
//
//  }

    // delete api
    @DeleteMapping("{checklistId}")
    public ResponseEntity<Void> deleteChecklistItem(@PathVariable Long taskId, @PathVariable Long checklistId){
        checklistItemService.deleteChecklistItem(taskId,checklistId);
        return ResponseEntity.noContent().build();
    }


    // log start of checklist item
    @PatchMapping("/{checklistId}/start")
    public ResponseEntity<ResponseDTO<ChecklistItemUpdateResponseDTO>> logStartOfChecklistItem(@PathVariable Long taskId, @PathVariable Long checklistId){
      ChecklistItemUpdateResponseDTO checklistItemUpdateResponseDTO = checklistItemService.logStartOfChecklistItem(taskId,checklistId);
      ResponseDTO<ChecklistItemUpdateResponseDTO> responseDTO= new ResponseDTO<>("Logged Checklist item as started successfully.",checklistItemUpdateResponseDTO);
      return new ResponseEntity<>(responseDTO, HttpStatus.OK);
  }




    // 6) MArk checklist item complete : Could provide in output: start time, end time and total time taken for checklist item
   @PatchMapping("/{checklistId}/mark-checklist-item-complete")
    public ResponseEntity<ResponseDTO<ChecklistItemUpdateResponseDTO>> markChecklistItemComplete(@PathVariable Long taskId, @PathVariable Long checklistId){
      ChecklistItemUpdateResponseDTO checklistItemUpdateResponseDTO = checklistItemService.markChecklistItemCompleted(taskId,checklistId);
      ResponseDTO<ChecklistItemUpdateResponseDTO> responseDTO = new ResponseDTO<>("Checklist Item Updated Successfully!", checklistItemUpdateResponseDTO);
      return new ResponseEntity<>(responseDTO, org.springframework.http.HttpStatus.OK);
    }





}
