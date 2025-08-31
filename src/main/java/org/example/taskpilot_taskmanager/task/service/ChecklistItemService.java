package org.example.taskpilot_taskmanager.task.service;

import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemCreateRequestDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemCreateResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemResponseDTO;
import org.example.taskpilot_taskmanager.task.dtos.ChecklistItemUpdateResponseDTO;

import java.util.List;

public interface ChecklistItemService {

    public List<ChecklistItemCreateResponseDTO> createChecklistItemsForTaskID(Long taskId,List<ChecklistItemCreateRequestDTO> checklistCreateDTOItems);

    public ChecklistItemResponseDTO getSingleChecklistItemByChecklistID(Long taskId, Long checklistId);

    public List<ChecklistItemResponseDTO> getAllChecklistItemsByTaskID(Long taskId);

    public void deleteChecklistItem(Long taskId, Long checklistId);

    public ChecklistItemUpdateResponseDTO markChecklistItemCompleted(Long taskId, Long checklistId);

    public ChecklistItemUpdateResponseDTO logStartOfChecklistItem(Long taskId, Long checklistId);

}
