package org.example.taskpilot_taskmanager.task.service;

import jakarta.transaction.Transactional;
import org.example.taskpilot_taskmanager.notifications.dispatch.UnifiedNotificationDispatcher;
import org.example.taskpilot_taskmanager.notifications.dtos.EmailRequestDTO;
import org.example.taskpilot_taskmanager.task.dtos.*;
import org.example.taskpilot_taskmanager.task.enums.TaskPriority;
import org.example.taskpilot_taskmanager.task.enums.TaskStatus;
import org.example.taskpilot_taskmanager.task.exceptions.*;
import org.example.taskpilot_taskmanager.task.models.Category;
import org.example.taskpilot_taskmanager.task.models.ChecklistItem;
import org.example.taskpilot_taskmanager.task.models.Task;
import org.example.taskpilot_taskmanager.task.repositories.CategoryRepository;
import org.example.taskpilot_taskmanager.task.repositories.ChecklistItemRepository;
import org.example.taskpilot_taskmanager.task.repositories.TaskRepository;
import org.example.taskpilot_taskmanager.task.specification.TaskSpecification;
import org.example.taskpilot_taskmanager.task.utils.DurationFormatterUtil;
import org.example.taskpilot_taskmanager.task.utils.TaskEntityDTOMapper;
import org.example.taskpilot_taskmanager.user.model.User;
import org.example.taskpilot_taskmanager.user.util.AuthenticatedUserUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskManageServiceImpl implements TaskManageService {


    private final TaskRepository taskRepository;


    private final CategoryRepository categoryRepository;

    private final ChecklistItemRepository checklistItemRepository;

    private final AuthenticatedUserUtil authenticatedUserUtil;
    private final UnifiedNotificationDispatcher unifiedNotificationDispatcher ;


    public TaskManageServiceImpl(TaskRepository taskRepository, CategoryRepository categoryRepository,ChecklistItemRepository checklistItemRepository, AuthenticatedUserUtil authenticatedUserUtil, UnifiedNotificationDispatcher unifiedNotificationDispatcher
    ) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.checklistItemRepository = checklistItemRepository;
        this.authenticatedUserUtil = authenticatedUserUtil;
        this.unifiedNotificationDispatcher =unifiedNotificationDispatcher;
    }



    ///create task method
    @Transactional
    public TaskCreateResponseDTO createTask(String name, String description, String categoryCode, TaskPriority taskPriority, TaskStatus taskStatus, TimeEstimateDTO timeEstimation){
        //get authenticated user
        User user = authenticatedUserUtil.getCurrentUser();
        System.out.println("id of current user: "+user.getId());
        Optional<Task> optionalTask = taskRepository.findByUserAndNameAndTaskStatusNot(user,name,TaskStatus.COMPLETED); // exclude status completed
        if(optionalTask.isPresent()){
            throw new DuplicateActiveTaskException("Active Task with same task name already exist for user: "+name);
        }

        // create a task and save it to db
        Task task= new Task();
        task.setUser(user);
        task.setName(name);
        task.setDescription(description);

        // handle category setting
        if(categoryCode==null ||  categoryCode.isBlank()) {
            //create default category for user
            Optional<Category> optionalCategory = categoryRepository.findByUserAndCode(user,"default-code");
            if(optionalCategory.isEmpty()){
                Category category = new Category();
                category.setUser(user);
                category.setCode("default-code");
                category.setName("Default");
                categoryRepository.save(category);
            }
            categoryCode="default-code";
        }

        String updatedCategoryCode=categoryCode; // to avoid issue with lambda
        Category category = categoryRepository.findByUserAndCode(user,updatedCategoryCode).orElseThrow(()-> new CategoryNotFoundException("Category not found with code: "+updatedCategoryCode));
        task.setCategory(category);

        task.setTaskPriority(taskPriority);
        if(taskStatus!=null){
            task.setTaskStatus(taskStatus);
        }

        task.setExpectedTaskTime(TaskEntityDTOMapper.convertTimeEstimateDTOToTimeEstimation(timeEstimation));

        Task savedTask= taskRepository.save(task);
        TaskCreateResponseDTO taskCreateResponseDTO = TaskEntityDTOMapper.convertTaskToTaskCreateResponseDTO(savedTask);

        //send email to user about task creation
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(user.getEmail());
        emailRequestDTO.setSubject("Task added successfully");
 /*       For HTML Email template:
        String content = "<p>Hi " + user.getName() + ",</p>"
                + "<p>Your recent Task with task id: <b>" + task.getId() + "</b> has been added successfully.</p>"
                + "<p><u>Details:</u></p>"
                + "<ul>"
                + "<li>Task Name: " + task.getName() + "</li>"
                + "<li>Task Description: " + task.getDescription() + "</li>"
                + "<li>Task Priority: " + task.getTaskPriority() + "</li>"
                + "<li>Category code: " + task.getCategory().getCode() + "</li>"
                + "<li>Expected time: " + DurationFormatterUtil.formatDuration(task.getExpectedTaskTime().toDuration()) + "</li>"
                + "</ul>";
        emailRequestDTO.setContent(content);  */

        String content = "Hi " + user.getName() + ",\n"
                + "Your recent Task with task id: "+task.getId()+ " has been added successfully.\n"
                + "Following are some details of the task:\n\n"
                + "Task Name: " + task.getName() + "\n"
                + "Task Description: " + task.getDescription() + "\n"
                + "Task Priority: " + task.getTaskPriority().toString() + "\n"
                + "Category code: " + task.getCategory().getCode() + "\n"
                + "Expected time : " + DurationFormatterUtil.formatDuration(task.getExpectedTaskTime().toDuration()) + "\n";


        emailRequestDTO.setContent(content);
        unifiedNotificationDispatcher.dispatch(emailRequestDTO);


        return taskCreateResponseDTO;
    }



    /// authenticated update task method
    @Transactional
    public TaskUpdateResponseDTO updateTaskById(Long taskId, String name, String description, String categoryCode, TaskPriority taskPriority, TaskStatus taskStatus, TimeEstimateDTO expectedTaskTime){
        //get authenticated user
        User user = authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask= taskRepository.findByIdAndUser(taskId,user);

        if(optionalTask.isEmpty()){
            throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }
        Task task= optionalTask.get();

        if(name!=null && !name.isEmpty()){
            task.setName(name);
        }
        if(description!=null && !description.isEmpty()){
            task.setDescription(description);
        }
        if(categoryCode!=null && !categoryCode.isEmpty()){
            Category category = categoryRepository.findByCode(categoryCode).orElseThrow(()-> new CategoryNotFoundException("Category not found with code: "+categoryCode));
            task.setCategory(category);
        }
        if(taskPriority!=null){
            task.setTaskPriority(taskPriority);
        }
        if(taskStatus!=null){
            task.setTaskStatus(taskStatus);
        }

        if(expectedTaskTime!=null){
            task.setExpectedTaskTime(TaskEntityDTOMapper.convertTimeEstimateDTOToTimeEstimation(expectedTaskTime));
        }


        Task updatedTask= taskRepository.save(task);

        TaskUpdateResponseDTO taskUpdateResponseDTO = TaskEntityDTOMapper.convertTaskToTaskUpdateResponseDTO(updatedTask);

        //send email to user about task updation
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(user.getEmail());
        emailRequestDTO.setSubject("Task updated successfully");
        StringBuilder content = new StringBuilder();
        content.append("Hi ").append(user.getName()).append(",\n")
                .append("Your recent Task with task id: " + task.getId()+ " has been updated successfully.\n")
                .append("Following are the details of the task updates:\n\n");
        if(name!=null && !name.isEmpty()){
            content.append("Task Name: ").append(name).append("\n");
        }
        if(description!=null && !description.isEmpty()){
            content.append("Task Description: ").append(description).append("\n");
        }
        if(categoryCode!=null && !categoryCode.isEmpty()){
            content.append("Category code: ").append(categoryCode).append("\n");
        }
        if(taskPriority!=null){
            content.append("Task Priority: ").append(taskPriority.toString()).append("\n");
        }
        if(taskStatus!=null){ // Todo Ideally should not allow update of status through update task api, should have dedicated apis to mark status of task
            content.append("Task Status: ").append(taskStatus.toString()).append("\n");
        }
        if(expectedTaskTime!=null){
            content.append("Expected time : ").append(DurationFormatterUtil.formatDuration(task.getExpectedTaskTime().toDuration())).append("\n\n");
        }
        content.append("Kindly make note of these changes. Thank you!\n\n");

        emailRequestDTO.setContent(content.toString());
        unifiedNotificationDispatcher.dispatch(emailRequestDTO);
        return taskUpdateResponseDTO;
    }




    ///authenticated get all tasks
    public List<TaskResponseDTO> getAllTasks(){
        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();
        List<Task> tasks= taskRepository.findAllByUser(user);
        if(tasks==null || tasks.isEmpty()){
            throw new TaskNotFoundException("No tasks found for given filter criteria for the user.");
        }
        List<TaskResponseDTO> taskResponseDTOlist = new ArrayList<>();
        for(Task task : tasks){
            TaskResponseDTO taskResponseDTO = TaskEntityDTOMapper.convertTaskToTaskResponseDTO(task);
            taskResponseDTOlist.add(taskResponseDTO);
        }
        return taskResponseDTOlist;
    }



   /// authenticated get paginated tasks
    public Page<TaskResponseDTO> getPaginatedTasks(int page, int size, String sortBy, String sortDirection){
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);  // Sort.Direction.fromString will throw error if invalid input is provided
        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();
        Page<Task> paginatedTasks= taskRepository.findAllByUser(user,PageRequest.of(page, size,sort));
        if(paginatedTasks.isEmpty()){
            throw new TaskNotFoundException("No tasks found for given filter criteria for the user.");
        }

        Page<TaskResponseDTO> paginatedTaskResponseDTOs =  paginatedTasks.map(task -> TaskEntityDTOMapper.convertTaskToTaskResponseDTO(task));

        return paginatedTaskResponseDTOs;
    }



    /// authenticated(user) GET complete filtered tasks (filter+ sort+ search+ pagination)
    public Page<TaskResponseDTO> getFilteredTasks(TaskFilterRequestDTO filterDTO) {

        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();

        Specification<Task> specification = new TaskSpecification(filterDTO);
        // filter further by User
        Specification<Task> finalUserBasedSpecification = specification.and(TaskSpecification.belongsToUser(user.getId()));

        Sort sort = "desc".equalsIgnoreCase(filterDTO.getSortDirection())
                ? Sort.by(filterDTO.getSortBy()).descending()
                : Sort.by(filterDTO.getSortBy()).ascending();

        Pageable pageable = PageRequest.of(filterDTO.getPage(), filterDTO.getSize(), sort);

        Page<Task> taskPage = taskRepository.findAll(finalUserBasedSpecification, pageable);
        if(taskPage.isEmpty()){
            throw new TaskNotFoundException("No tasks found for given filter criteria for the user.");
        }

       Page<TaskResponseDTO> paginatedTaskResponseDTOs =  taskPage.map(task -> TaskEntityDTOMapper.convertTaskToTaskResponseDTO(task));

        return paginatedTaskResponseDTOs;
    }





    /// authenticated get single task
    public TaskResponseDTO getSingleTaskByID(Long id){
        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();

        Optional<Task> optionalTask = taskRepository.findByIdAndUser(id,user);
        if(optionalTask.isPresent() && optionalTask.get().getUser().equals(user)) {
            Task task = optionalTask.get();
            TaskResponseDTO taskResponseDTO = TaskEntityDTOMapper.convertTaskToTaskResponseDTO(task);
            return taskResponseDTO;
        }
        else {
            throw new TaskNotFoundException("Task with given ID not found: "+id);
        }
    }




    /// authenticated delete task
    public void deleteTask(Long taskId){
        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask= taskRepository.findByIdAndUser(taskId,user);
        if(optionalTask.isEmpty()){
            throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }
        Task task= optionalTask.get();
        taskRepository.delete(task);
        //send email to user about task deletion
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(user.getEmail());
        emailRequestDTO.setSubject("Task deleted successfully");
        StringBuilder content = new StringBuilder();
        content.append("Hi ").append(user.getName()).append(",\n")
                .append("Your recent Task with task id: ").append(task.getId()).append(" - '").append(task.getName()).append("' has been deleted successfully.\n\n");
        emailRequestDTO.setContent(content.toString());
        unifiedNotificationDispatcher.dispatch(emailRequestDTO);
    }







/// advanced task apis

    // log task started
    public TaskUpdateResponseDTO logTaskStart(Long taskId){
        //get authenticated user
        User user= authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask= taskRepository.findByIdAndUser(taskId,user);
        if(optionalTask.isEmpty()){
            throw new TaskNotFoundException("Task not found with given id: "+taskId);
        }
        Task task= optionalTask.get();
        if(task.getStartDateTime()!=null){
            throw new TaskAlreadyStartedException("Task with task id : \""+taskId+"\" is already logged as Started.");
        }

        task.setStartDateTime(LocalDateTime.now());
        task.setTaskStatus(TaskStatus.INPROGRESS);
        Task updatedTask= taskRepository.save(task);
        TaskUpdateResponseDTO taskUpdateResponseDTO = TaskEntityDTOMapper.convertTaskToTaskUpdateResponseDTO(updatedTask);

        //send email to user about task start
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(user.getEmail());
        emailRequestDTO.setSubject("Task : "+task.getName()+ " started successfully");
        StringBuilder content = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        LocalDateTime startDateTime = task.getStartDateTime();
        LocalDateTime expectedEndDateTime = startDateTime.plus(task.getExpectedTaskTime().toDuration());

        content.append("Hi ").append(user.getName()).append(",\n")
                .append("Your recent Task with task id: ").append(task.getId()).append(": ").append(task.getName()).append(" has been marked started successfully.\n\n")
                .append("Start time: ").append(task.getStartDateTime().format(formatter)).append("\n\n")
        .append("Your expected time to complete this task is : ").append(DurationFormatterUtil.formatDuration(task.getExpectedTaskTime().toDuration())).append(" that is effectively by: ").append(expectedEndDateTime.format(formatter)).append("\n")
        .append("All the Best !");
        emailRequestDTO.setContent(content.toString());
        unifiedNotificationDispatcher.dispatch(emailRequestDTO);
         return taskUpdateResponseDTO;
    }



    // mark-task-completed
    @Transactional
    public TaskUpdateResponseDTO markTaskCompleted(Long taskId) {
        //get authenticated user
        User user = authenticatedUserUtil.getCurrentUser();
        Optional<Task> optionalTask = taskRepository.findByIdAndUser(taskId, user);
        if (optionalTask.isEmpty()) {
            throw new TaskNotFoundException("Task not found with given id: " + taskId);
        }
        if (optionalTask.get().getTaskStatus().equals(TaskStatus.COMPLETED)) {
            throw new TaskAlreadyCompletedException("Provided task with taskId: \"" + taskId + "\" is already marked as completed.");
        }

        Task task = optionalTask.get();
        //set end time and status completed for task
        task.setEndDateTime(LocalDateTime.now());
        task.setTaskStatus(TaskStatus.COMPLETED);

        // set total time taken
        if (task.getStartDateTime() != null && task.getEndDateTime() != null) {
            Duration completionDuration = Duration.between(task.getStartDateTime(), task.getEndDateTime());
            task.setTotalTimeTaken(completionDuration);
        } else {
            // check if start time already set or not, if not ,set it to createdAt time and in message provide detail that it was not set.
            task.setStartDateTime(task.getCreatedAt()); // TODO check if updatedAt is same as endTask
            Duration completionDuration = Duration.between(task.getStartDateTime(), task.getEndDateTime());
            task.setTotalTimeTaken(completionDuration);
            task.setTaskRemark("Task did not have a start time set, so total time taken is calculated based on createdAt time.");

        }
        // mark checklist completed
        List<ChecklistItem> checklistItems = checklistItemRepository.findAllByTaskIdAndTask_User(taskId, user);
        checklistItems.forEach(checklistItem -> checklistItem.setCompleted(true));
        checklistItems.forEach(checklistItem -> {
            if (checklistItem.getStartDateTime() == null) {
                checklistItem.setStartDateTime(LocalDateTime.now());
            }
        });
        checklistItems.forEach(checklistItem -> {
            if (checklistItem.getEndDateTime() == null) {
                checklistItem.setEndDateTime(LocalDateTime.now());
            }
        });
        checklistItemRepository.saveAll(checklistItems);
        // calculate total time and display in response message
        Duration taskExpectedDuration = task.getExpectedTaskTime().toDuration();  // Duration.ofDays(task.getExpectedTaskTime().getDays()).plusHours(task.getExpectedTaskTime().getHours()).plusMinutes(task.getExpectedTaskTime().getMinutes());
        task.setTaskRemark((task.getTaskRemark() != null ? task.getTaskRemark() : "") + DurationFormatterUtil.generateTimeSummaryMessage("Task", task.getTotalTimeTaken(), taskExpectedDuration));
        Task updatedTask = taskRepository.save(task);
        TaskUpdateResponseDTO taskUpdateResponseDTO = TaskEntityDTOMapper.convertTaskToTaskUpdateResponseDTO(updatedTask);
        //delegate email logic to dedicated method
        handleEmailForTaskCompleted(task,user);

        return taskUpdateResponseDTO;
    }



    private void handleEmailForTaskCompleted(Task task,User user){
        //send email to user about task completion
        //Todo can possibly move this to a separate method so that it can be called when all checklist items are marked complete
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(user.getEmail());
        emailRequestDTO.setSubject("Task : " + task.getName() + " marked complete");
        StringBuilder content = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

        content.append("Hi ").append(user.getName()).append(",\n")
                .append("Your recent Task with task id: ").append(task.getId()).append("- ").append(task.getName()).append(" has been marked completed successfully.\n\n") ;
        if(task.getTaskRemark()!=null && !task.getTaskRemark().isBlank()){
            content.append("Note: ").append(task.getTaskRemark()).append("\n\n");
        }
            content.append("Here are your stats for the task: \n")
                .append("Task Name: ").append(task.getName()).append("\n")
                .append("Task Description: ").append(task.getDescription()).append("\n")
                .append("Task started on: ").append(task.getStartDateTime().format(formatter)).append("\n")
                .append("Task ended on: ").append(task.getEndDateTime().format(formatter)).append("\n")
                .append("Expected Task time: ").append(DurationFormatterUtil.formatDuration(task.getExpectedTaskTime().toDuration())).append("\n")
                .append("Actual time taken: ").append(DurationFormatterUtil.formatDuration(task.getTotalTimeTaken())).append("\n\n");
        List<ChecklistItem> associatedChecklistItems = checklistItemRepository.findAllByTaskIdAndTask_User(task.getId(), user);
        int val=0;
        if (!associatedChecklistItems.isEmpty()) {
            content.append("Here are the details of the checklist items associated with this task: \n");
            for (ChecklistItem checklistItem : associatedChecklistItems) {
                content.append(val++).append(") ");
                content.append("Item id: ").append(checklistItem.getId()).append(" - ").append(checklistItem.getLabel()).append("\n");
                content.append("Item expected time: ").append(DurationFormatterUtil.formatDuration(checklistItem.getExpectedTaskTime().toDuration())).append("\n")
                        .append("Item actual time taken: ").append(DurationFormatterUtil.formatDuration(checklistItem.getTotalTimeTaken())).append("\n");
            }
        }
        else {
            content.append("This task had no associated checklist items.\n\n");
        }
        emailRequestDTO.setContent(content.toString());
        unifiedNotificationDispatcher.dispatch(emailRequestDTO);
    }



//    /// mark all tasks completed -- extra feature
//    public List<TaskDTO> markAllCompleted(){
//
//    }

   }



