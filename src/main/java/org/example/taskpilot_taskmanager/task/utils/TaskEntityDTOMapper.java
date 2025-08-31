package org.example.taskpilot_taskmanager.task.utils;

import org.example.taskpilot_taskmanager.task.dtos.*;
import org.example.taskpilot_taskmanager.task.models.Category;
import org.example.taskpilot_taskmanager.task.models.Task;
import org.example.taskpilot_taskmanager.task.models.ChecklistItem;
import org.example.taskpilot_taskmanager.task.models.TimeEstimation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
public class TaskEntityDTOMapper {


    //---task mapper

    public static TaskCreateResponseDTO convertTaskToTaskCreateResponseDTO(Task task){
        TaskCreateResponseDTO taskCreateResponseDTO = new TaskCreateResponseDTO();
        taskCreateResponseDTO.setTaskId(task.getId());
        taskCreateResponseDTO.setName(task.getName());
        taskCreateResponseDTO.setDescription(task.getDescription());
        taskCreateResponseDTO.setCategory(convertCategoryToCategoryResponseDTO(task.getCategory()));
        taskCreateResponseDTO.setTaskPriority(task.getTaskPriority());
        taskCreateResponseDTO.setStatus(task.getTaskStatus());
//        List<ChecklistItemCreateResponseDTO> taskChecklistCreateResponseDTOItems = convertChecklistItemListToChecklistItemCreateResponseDTO(task.getTaskChecklistItems());
//        taskCreateResponseDTO.setChecklistItems(taskChecklistCreateResponseDTOItems);
        TimeEstimateDTO timeEstimateDTO = convertTimeEstimationToTimeEstimateDTO(task.getExpectedTaskTime());
        taskCreateResponseDTO.setExpectedTaskTime(timeEstimateDTO);
        taskCreateResponseDTO.setCreatedAt(task.getCreatedAt());
        return taskCreateResponseDTO;
    }


    public static TaskResponseDTO convertTaskToTaskResponseDTO(Task task){
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setRemarkMessage(task.getTaskRemark());
        taskResponseDTO.setTaskId(task.getId());
        taskResponseDTO.setTimestamp(LocalDateTime.now());
        taskResponseDTO.setName(task.getName());
        taskResponseDTO.setDescription(task.getDescription());
        taskResponseDTO.setCategoryCode(task.getCategory().getCode());
        taskResponseDTO.setTaskPriority(task.getTaskPriority());
        taskResponseDTO.setTaskStatus(task.getTaskStatus());
//        List<ChecklistItemCreateResponseDTO> taskChecklistCreateResponseDTOItems = convertChecklistItemListToChecklistItemCreateResponseDTO(task.getTaskChecklistItems());
//        taskCreateResponseDTO.setChecklistItems(taskChecklistCreateResponseDTOItems);
        TimeEstimateDTO timeEstimateDTO = convertTimeEstimationToTimeEstimateDTO(task.getExpectedTaskTime());
        taskResponseDTO.setExpectedTaskTime(timeEstimateDTO);
        if(task.getStartDateTime()!=null) {
            taskResponseDTO.setStartTime(task.getStartDateTime());
        }
        if(task.getEndDateTime()!=null) {
            taskResponseDTO.setEndTime(task.getEndDateTime());
        }
        if(task.getTotalTimeTaken()!=null) {
            taskResponseDTO.setCompletionTime(task.getTotalTimeTaken());
        }

        return taskResponseDTO;
    }


    public static TaskUpdateResponseDTO convertTaskToTaskUpdateResponseDTO(Task task){
        TaskUpdateResponseDTO taskUpdateResponseDTO = new TaskUpdateResponseDTO();
        taskUpdateResponseDTO.setRemarkMessage(task.getTaskRemark());
        taskUpdateResponseDTO.setTaskId(task.getId());
        taskUpdateResponseDTO.setName(task.getName());
        taskUpdateResponseDTO.setDescription(task.getDescription());
        taskUpdateResponseDTO.setCategoryCode(task.getCategory().getCode());
        taskUpdateResponseDTO.setTaskPriority(task.getTaskPriority());
        taskUpdateResponseDTO.setStatus(task.getTaskStatus());
        taskUpdateResponseDTO.setExpectedTaskTime(convertTimeEstimationToTimeEstimateDTO(task.getExpectedTaskTime()));
        taskUpdateResponseDTO.setUpdatedAt(task.getUpdatedAt());

        // set start time, end time , total time /time taken till now
        if(task.getStartDateTime()!=null) {
            taskUpdateResponseDTO.setStartTime(task.getStartDateTime());
        }
        if(task.getEndDateTime()!=null) {
            taskUpdateResponseDTO.setEndTime(task.getEndDateTime());
        }
        if(task.getTotalTimeTaken()!=null) {
            taskUpdateResponseDTO.setCompletionTime(task.getTotalTimeTaken());
        }

        return taskUpdateResponseDTO;
    }





    //-----category mapper


    public static CategoryCreateResponseDTO convertCategoryToCategoryCreateResponseDTO(Category category){
        CategoryCreateResponseDTO categoryCreateResponseDTO = new CategoryCreateResponseDTO();

        categoryCreateResponseDTO.setCreatedAt(category.getCreatedAt());
        categoryCreateResponseDTO.setCategoryId(category.getId());
        categoryCreateResponseDTO.setName(category.getName());
        categoryCreateResponseDTO.setDescription(category.getDescription());
        categoryCreateResponseDTO.setCategoryCode(category.getCode());
        categoryCreateResponseDTO.setCategoryType(category.getCategoryType());
        return categoryCreateResponseDTO;
    }




    public static CategoryResponseDTO convertCategoryToCategoryResponseDTO(Category category){
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        categoryResponseDTO.setName(category.getName());
        categoryResponseDTO.setDescription(category.getDescription());
        categoryResponseDTO.setCategoryCode(category.getCode());
        categoryResponseDTO.setCategoryType(category.getCategoryType());
        return categoryResponseDTO;
    }

    public static CategoryUpdateResponseDTO convertCategoryToCategoryUpdateResponseDTO(Category category){
        CategoryUpdateResponseDTO categoryUpdateResponseDTO = new CategoryUpdateResponseDTO();

        categoryUpdateResponseDTO.setUpdatedAt(category.getUpdatedAt());
        categoryUpdateResponseDTO.setCategoryId(category.getId());
        categoryUpdateResponseDTO.setName(category.getName());
        categoryUpdateResponseDTO.setDescription(category.getDescription());
        categoryUpdateResponseDTO.setCategoryCode(category.getCode());
        categoryUpdateResponseDTO.setCategoryType(category.getCategoryType());
        return categoryUpdateResponseDTO;
    }





   //----- checklist mapper


    public static List<ChecklistItemCreateResponseDTO> convertChecklistItemListToChecklistItemCreateResponseDTO(List<ChecklistItem> checklistItemList){
        List<ChecklistItemCreateResponseDTO> checklistItemCreateResponseDTOList= new ArrayList<>();

        for(ChecklistItem taskChecklistItem : checklistItemList){
            ChecklistItemCreateResponseDTO checklistItemCreateResponseDto = new ChecklistItemCreateResponseDTO();
            checklistItemCreateResponseDto.setChecklistId(taskChecklistItem.getId());
            checklistItemCreateResponseDto.setLabel(taskChecklistItem.getLabel());
            checklistItemCreateResponseDto.setCompleted(taskChecklistItem.isCompleted()); // for boolean getter, setter conventions are different, see overview
            checklistItemCreateResponseDto.setDescription(taskChecklistItem.getDescription());
            checklistItemCreateResponseDto.setCreatedAt(taskChecklistItem.getCreatedAt());
            checklistItemCreateResponseDto.setExpectedTaskTime(convertTimeEstimationToTimeEstimateDTO(taskChecklistItem.getExpectedTaskTime()));
            checklistItemCreateResponseDTOList.add(checklistItemCreateResponseDto);
        }

        return checklistItemCreateResponseDTOList;
    }


    public static List<ChecklistItemResponseDTO> convertChecklistItemListToChecklistItemResponseDTOList(List<ChecklistItem> checklistItemList){
        List<ChecklistItemResponseDTO> checklistItemResponseDTOList= new ArrayList<>();
        for(ChecklistItem checklistItem : checklistItemList){
            ChecklistItemResponseDTO checklistItemResponseDTO = convertChecklistItemToChecklistItemResponseDTO(checklistItem);
            checklistItemResponseDTOList.add(checklistItemResponseDTO);
        }
        return checklistItemResponseDTOList;
    }


    public static ChecklistItemResponseDTO convertChecklistItemToChecklistItemResponseDTO(ChecklistItem checklistItem){
        ChecklistItemResponseDTO checklistItemResponseDTO= new ChecklistItemResponseDTO();
        checklistItemResponseDTO.setRemarkMessage(checklistItem.getChecklistRemark());
        checklistItemResponseDTO.setChecklistId(checklistItem.getId());
        checklistItemResponseDTO.setLabel(checklistItem.getLabel());
        checklistItemResponseDTO.setCompleted(checklistItem.isCompleted());
        checklistItemResponseDTO.setDescription(checklistItem.getDescription());
        checklistItemResponseDTO.setExpectedTaskTime(convertTimeEstimationToTimeEstimateDTO(checklistItem.getExpectedTaskTime()));

        return checklistItemResponseDTO;
    }

    public static ChecklistItemUpdateResponseDTO convertChecklistItemToChecklistItemUpdateResponseDTO(ChecklistItem checklistItem){
        ChecklistItemUpdateResponseDTO checklistItemUpdateResponseDTO= new ChecklistItemUpdateResponseDTO();
       checklistItemUpdateResponseDTO.setRemarkMessage(checklistItem.getChecklistRemark());
        checklistItemUpdateResponseDTO.setChecklistId(checklistItem.getId());
        checklistItemUpdateResponseDTO.setLabel(checklistItem.getLabel());
        checklistItemUpdateResponseDTO.setCompleted(checklistItem.isCompleted());
        checklistItemUpdateResponseDTO.setDescription(checklistItem.getDescription());
        checklistItemUpdateResponseDTO.setExpectedTaskTime(convertTimeEstimationToTimeEstimateDTO(checklistItem.getExpectedTaskTime()));
        checklistItemUpdateResponseDTO.setUpdatedAt(checklistItem.getUpdatedAt());
        checklistItemUpdateResponseDTO.setStartTime(checklistItem.getStartDateTime());
        checklistItemUpdateResponseDTO.setEndTime(checklistItem.getEndDateTime());
        checklistItemUpdateResponseDTO.setCompletionTime(checklistItem.getTotalTimeTaken());
        return checklistItemUpdateResponseDTO;

    }




    public static List<ChecklistItem> convertChecklistCreateResponseDtoListToChecklistItemsList(List<ChecklistItemCreateResponseDTO> checklistItemCreateResponseDTOList){
        List<ChecklistItem> checklistItems = new ArrayList<>();

        for(ChecklistItemCreateResponseDTO checklistItemCreateResponseDTO : checklistItemCreateResponseDTOList){
            ChecklistItem checklistItem= new ChecklistItem();
            checklistItem.setId(checklistItemCreateResponseDTO.getChecklistId());
            checklistItem.setLabel(checklistItemCreateResponseDTO.getLabel());
            checklistItem.setDescription(checklistItemCreateResponseDTO.getDescription());
            if(checklistItemCreateResponseDTO.getCompleted()!=null){
            checklistItem.setCompleted(checklistItemCreateResponseDTO.getCompleted());
            }
            checklistItem.setExpectedTaskTime(convertTimeEstimateDTOToTimeEstimation(checklistItemCreateResponseDTO.getExpectedTaskTime()));
//            checklistItem.setTask(checklistItemCreateResponseDTO.getTask());
            checklistItems.add(checklistItem);

        }
        return checklistItems;
    }



    public static ChecklistItem convertChecklistItemCreateRequestDTOToChecklistItem(ChecklistItemCreateRequestDTO checklistItemCreateRequestDTO){
        ChecklistItem checklistItem= new ChecklistItem();
        checklistItem.setLabel(checklistItemCreateRequestDTO.getLabel());
        if(checklistItemCreateRequestDTO.getCompleted()!=null) {
            checklistItem.setCompleted(checklistItemCreateRequestDTO.getCompleted());
        }
        checklistItem.setDescription(checklistItemCreateRequestDTO.getDescription());
        return checklistItem;

    }





    //---- Time estimation mapper

    public static TimeEstimation convertTimeEstimateDTOToTimeEstimation(TimeEstimateDTO timeEstimateDto){
        TimeEstimation timeEstimation = new TimeEstimation();
        if(timeEstimateDto.getDays()!=null){
            timeEstimation.setDays(timeEstimateDto.getDays());
        }
       if(timeEstimateDto.getHours()!=null) {
           timeEstimation.setHours(timeEstimateDto.getHours());
       }
       if(timeEstimateDto.getMinutes()!=null) {
           timeEstimation.setMinutes(timeEstimateDto.getMinutes());
       }
        return timeEstimation;
    }


    public static TimeEstimateDTO convertTimeEstimationToTimeEstimateDTO(TimeEstimation timeEstimation){
        TimeEstimateDTO timeEstimateDto = new TimeEstimateDTO();
        timeEstimateDto.setDays(timeEstimation.getDays());
        timeEstimateDto.setHours(timeEstimation.getHours());
        timeEstimateDto.setMinutes(timeEstimation.getMinutes());
        return timeEstimateDto;
    }






//
//    public static UserDTO convertUserToUserDTO(User user){
//      UserDTO userDTO = new UserDTO();
//       userDTO.setUsername(user.getUsername());
//       userDTO.setEmail(user.getEmail());
//       return userDTO;
//    }




//    public static TimeEstimateDTO convertTimeEstimateToDTO(TimeEstimation timeEstimate){
//        TimeEstimateDTO timeEstimateDTO= new TimeEstimateDTO();
//        timeEstimateDTO.setDays(timeEstimate.getDays());
//        timeEstimateDTO.setHours(timeEstimate.getHours());
//        timeEstimateDTO.setMinutes(timeEstimate.getMinutes());
//        return timeEstimateDTO;
//    }





//    private static List<ChecklistItemCreateResponseDTO> buildChecklistItemsToChecklistCreateResponseDTOList(List<ChecklistItem> taskChecklistItems){
//    List<ChecklistItemCreateResponseDTO> taskChecklistCreateResponseDTOItems = new ArrayList<>();
//    for(ChecklistItem taskChecklistItem : taskChecklistItems){
//        ChecklistItemCreateResponseDTO taskChecklistDTOItem= TaskEntityDTOMapper.convertChecklistItemToChecklistItemCreateResponseDTO(taskChecklistItem);
//        taskChecklistCreateResponseDTOItems.add(taskChecklistDTOItem);
//    }
//    return taskChecklistCreateResponseDTOItems;
//    }


   }





// Note: Instead of manual converting to DTO from model, "Mapstruct" library can be utlized to automate mapping:
//    @Mapper(componentModel = "spring")
//    public interface TaskMapper {
//      TaskDTO toTaskDTO(Task task);  // automatically understands what to map
//    }
//OR simply ModelMapper instance
    //ModelMapper modelMapper = new ModelMapper();
    //TaskDTO dto = modelMapper.map(task, TaskDTO.class);

