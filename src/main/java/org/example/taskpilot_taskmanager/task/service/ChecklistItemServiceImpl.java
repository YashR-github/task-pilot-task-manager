package org.example.taskpilot_taskmanager.task.service;


import jakarta.transaction.Transactional;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemCreateRequestDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemCreateResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemUpdateResponseDTO;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;
import org.example.taskpilot_taskmanager.task.exceptions.*;
import org.example.taskpilot_taskmanager.task.models.Task;
import org.example.taskpilot_taskmanager.task.models.ChecklistItem;
import org.example.taskpilot_taskmanager.task.repositories.ChecklistItemRepository;
import org.example.taskpilot_taskmanager.task.repositories.TaskRepository;
import org.example.taskpilot_taskmanager.task.utils.DurationFormatterUtil;
import org.example.taskpilot_taskmanager.task.utils.TaskEntityDTOMapper;
import org.example.taskpilot_taskmanager.user.model.User;
import org.example.taskpilot_taskmanager.user.util.AuthenticatedUserUtil;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChecklistItemServiceImpl implements ChecklistItemService{


    private final  ChecklistItemRepository checklistItemRepository;
    private final TaskRepository taskRepository;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    public ChecklistItemServiceImpl(ChecklistItemRepository checklistItemRepository, TaskRepository taskRepository, AuthenticatedUserUtil authenticatedUserUtil) {
        this.checklistItemRepository = checklistItemRepository;
        this.taskRepository = taskRepository;
        this.authenticatedUserUtil = authenticatedUserUtil;
    }



    @Transactional
    public List<ChecklistItemCreateResponseDTO> createChecklistItemsForTaskID(Long taskId, List<ChecklistItemCreateRequestDTO> checklistCreateDTOItems){
       //add user based validation
        User user = authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask= taskRepository.findByIdAndUser(taskId,user);

        if(optionalTask.isEmpty()){
            throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }
        //assign checklists to task and also check for expected time violation for task
        List<ChecklistItem> updatedChecklistItemsWithTask = buildChecklistItemsWithTask(taskId,checklistCreateDTOItems);
        checklistItemRepository.saveAll(updatedChecklistItemsWithTask);

        return TaskEntityDTOMapper.convertChecklistItemListToChecklistItemCreateResponseDTO(updatedChecklistItemsWithTask);
    }


    // TODO update checklistItems




    public ChecklistItemResponseDTO getSingleChecklistItemByChecklistID(Long taskId, Long checklistId){
        // fetch auth user
        User user = authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask = taskRepository.findByIdAndUser(taskId,user);
        if(optionalTask.isEmpty()) {
        throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }
        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findByIdAndTask_UserAndTask_Id(checklistId,user,taskId);
        if (optionalChecklistItem.isEmpty()) {
            throw new ChecklistItemNotFoundException("ChecklistItem not found with given id: "+checklistId);
        }

        return TaskEntityDTOMapper.convertChecklistItemToChecklistItemResponseDTO(optionalChecklistItem.get());

    }



  //get all TODO filteration
    public List<ChecklistItemResponseDTO> getAllChecklistItemsByTaskID(Long taskId){
        // fetch auth user
        User user = authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask = taskRepository.findByIdAndUser(taskId,user);
        if(optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }
        List<ChecklistItem> checklistItemList = checklistItemRepository.findAllByTaskIdAndTask_User(taskId,user);

        // TODO add filter functionality

        return TaskEntityDTOMapper.convertChecklistItemListToChecklistItemResponseDTOList(checklistItemList);

    }


    //delete
    @Transactional
    public void deleteChecklistItem(Long taskId, Long checklistId){
        // fetch auth user
        User user = authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask = taskRepository.findByIdAndUser(taskId,user);
        if(optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }
        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findByIdAndTask_UserAndTask_Id(checklistId,user,taskId);
        if(optionalChecklistItem.isEmpty()){
            throw new ChecklistItemNotFoundException("ChecklistItem not found with given id: "+checklistId);
        }
        ChecklistItem checklistItem = optionalChecklistItem.get();
        Task task=optionalTask.get();
        //get current checklistItem estimated time and subtract from task
        Duration currentChecklistItemTimeEstimate = checklistItem.getExpectedTaskTime().toDuration();
        task.subtractChecklistEstimate(currentChecklistItemTimeEstimate);
        taskRepository.save(task);
        checklistItemRepository.deleteById(checklistId);
    }



    /// extra features below:

    //log checklist start
    @Transactional
    public ChecklistItemUpdateResponseDTO logStartOfChecklistItem(Long taskId, Long checklistId){
        // fetch auth user
        User user = authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask = taskRepository.findByIdAndUser(taskId,user);
        if(optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }

        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findByIdAndTask_UserAndTask_Id(checklistId,user,taskId);
        if(optionalChecklistItem.isEmpty()) {
            throw new ChecklistItemNotFoundException("ChecklistItem not found with given id: "+checklistId);
        }

        // check if checklist item is already started ie, startTime not null
        if(optionalChecklistItem.get().getStartDateTime()!=null) {
            throw new ChecklistItemAlreadyStartedException("Checklist item is already logged as Started.");
        }
        // since checklist item started , task can be marked as started
        Task task= optionalTask.get();
        if(task.getStartDateTime()==null) {
            task.setStartDateTime(LocalDateTime.now());
            task.setTaskStatus(TaskStatus.INPROGRESS);
            taskRepository.save(task);
        }
        ChecklistItem checklistItem = optionalChecklistItem.get();
        checklistItem.setStartDateTime(LocalDateTime.now());
        ChecklistItem savedChecklistItem= checklistItemRepository.save(checklistItem);
        return TaskEntityDTOMapper.convertChecklistItemToChecklistItemUpdateResponseDTO(savedChecklistItem);

    }



    @Transactional
    public ChecklistItemUpdateResponseDTO markChecklistItemCompleted(Long taskId, Long checklistId){
        // fetch auth user
        User user = authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask = taskRepository.findByIdAndUser(taskId,user);
        if(optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }
        Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findByIdAndTask_UserAndTask_Id(checklistId,user,taskId);
        if(optionalChecklistItem.isEmpty()) {
            throw new ChecklistItemNotFoundException("ChecklistItem not found with given id: "+checklistId);
        }
        if(optionalChecklistItem.get().isCompleted()){
            throw new ChecklistItemAlreadyCompletedException("Checklist with id: "+checklistId+" is already marked completed.");
        }
        Task task= optionalTask.get();
        //extra check
        if(task.getTaskStatus().equals(TaskStatus.COMPLETED)) {
            throw new InvalidTaskCompletedBeforeChecklistException("Task is already marked as Completed before ChecklistItem: "+checklistId+" is marked as Completed.");
        }

        ChecklistItem checklistItem = optionalChecklistItem.get();
        checklistItem.setCompleted(true);
        checklistItem.setEndDateTime(LocalDateTime.now());

        if(checklistItem.getStartDateTime()==null) {
            checklistItem.setStartDateTime(checklistItem.getCreatedAt());
        }
        checklistItem.setTotalTimeTaken(Duration.between(checklistItem.getStartDateTime(), checklistItem.getEndDateTime()));

        // extra- if checklist is last one to complete , mark task as completed ;using jpa method fetch checklist having complete=false exist or not , if not, mark task completed
        List<ChecklistItem> checklistItems= checklistItemRepository.findAllByTask_IdAndTask_UserAndCompleted(taskId,user,false);
        if(checklistItems==null || checklistItems.isEmpty()){
            task.setTaskStatus(TaskStatus.COMPLETED);
            task.setEndDateTime(LocalDateTime.now());
            Duration totalCompletionTime = Duration.between(task.getStartDateTime(), task.getEndDateTime());
            task.setTotalTimeTaken(totalCompletionTime);
            task.setTaskRemark((task.getTaskRemark()!=null ? task.getTaskRemark():"")+ "Since the last checklist item : "+checklistItem.getLabel()+ " was marked completed, this task is marked as complete.");
            taskRepository.save(task);
            checklistItem.setChecklistRemark("This was the last checklist item. Since this is marked completed, you have now completed the Task : "+taskId+" : "+task.getName());
        }
        // add a message stating the time taken extra to complete the checklist item or the whole task if all checklist completed
        Duration checklistItemExpectedDuration= Duration.ofDays(checklistItem.getExpectedTaskTime().getDays()).plusHours(checklistItem.getExpectedTaskTime().getHours()).plusMinutes(checklistItem.getExpectedTaskTime().getMinutes());
        checklistItem.setChecklistRemark((checklistItem.getChecklistRemark()!=null ? checklistItem.getChecklistRemark():"" )+ DurationFormatterUtil.generateTimeSummaryMessage("Checklist item",checklistItem.getTotalTimeTaken(),checklistItemExpectedDuration));
        checklistItemRepository.save(checklistItem);
        return TaskEntityDTOMapper.convertChecklistItemToChecklistItemUpdateResponseDTO(checklistItem);

    }











    ///helper method
    @Transactional
    public  List<ChecklistItem> buildChecklistItemsWithTask(Long taskId, List<ChecklistItemCreateRequestDTO> checklistCreateRequestDTOList){
        // fetch auth user
        User user = authenticatedUserUtil.getCurrentUser();
        List<ChecklistItem> updatedChecklistItems = new ArrayList<>();
        for(ChecklistItemCreateRequestDTO checklistItemRequestDTO : checklistCreateRequestDTOList){
            //check if checklist item already exist by name
            Optional<ChecklistItem> optionalChecklistItem = checklistItemRepository.findByTask_IdAndLabelAndTask_User(taskId,checklistItemRequestDTO.getLabel(),user);
            if(optionalChecklistItem.isPresent()){
                throw new ChecklistItemAlreadyExistException("ChecklistItem with name: \""+checklistItemRequestDTO.getLabel()+"\" already exist for the task .Please change names to avoid duplicate entries.");
            }

            ChecklistItem checklistItem= TaskEntityDTOMapper.convertChecklistItemCreateRequestDTOToChecklistItem(checklistItemRequestDTO);
            checklistItem.setTask(taskRepository.findByIdAndUser(taskId,user).get()); //assuming task exist for taskId is handled at service method level  // set parent for correct mapping , no need for save here as persistance is cascade all
            // set checklistItemsEstimateDuration field based on every checklist item
            //TODO maybe instead of below code, make methods each for toDuration(TimeEstimate){In TimeEstimate class}, {In task class} addToChecklistEstimate(Duration toAdd), subtractFromChecklistEstimate(Duration toSubtract)
            Task task = taskRepository.findByIdAndUser(taskId,user).get();
            Duration currentChecklistItemDuration =  checklistItem.getExpectedTaskTime().toDuration();
            task.addChecklistEstimate(currentChecklistItemDuration);
            //exception to throw if checklistEstimate exceeding task estimated time
            if(task.getChecklistItemsEstimateDuration().compareTo(task.getExpectedTaskTime().toDuration())>0) {
                throw new ChecklistTimeExceedsException("The estimated time for this task is "+ DurationFormatterUtil.formatDuration(task.getExpectedTaskTime().toDuration())+", but the total time taken by all checklist items is "+DurationFormatterUtil.formatDuration(task.getChecklistItemsEstimateDuration())+". Please increase the estimated time for the main task or if applicable decrease the checklist item time estimate");
            }
            taskRepository.save(task);
            updatedChecklistItems.add(checklistItem);
        }
        return updatedChecklistItems;
    }




}
