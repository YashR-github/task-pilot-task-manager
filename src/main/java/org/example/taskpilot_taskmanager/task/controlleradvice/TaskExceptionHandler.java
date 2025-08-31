package org.example.taskpilot_taskmanager.task.controlleradvice;

import org.example.taskpilot_taskmanager.common.dtos.ResponseDTO;
import org.example.taskpilot_taskmanager.task.exceptions.*;
import org.example.taskpilot_taskmanager.user.exceptions.UserNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice(basePackages= {"org.example.codetodoapplicationpersonal.task"})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TaskExceptionHandler {



    //-----Task Exceptions
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleTaskNotFoundException(TaskNotFoundException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Task Not Found");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DuplicateActiveTaskException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleDuplicateActiveTaskException(DuplicateActiveTaskException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Task Already exists");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(TaskAlreadyStartedException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleTaskAlreadyStartedException(TaskAlreadyStartedException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Task Already marked as Started.");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskAlreadyCompletedException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleTaskAlreadyCompletedException(TaskAlreadyCompletedException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Task Already marked as Completed.");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(InvalidTaskCompletedBeforeChecklistException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleInvalidTaskCompletedBeforeChecklistException(InvalidTaskCompletedBeforeChecklistException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Task Marked as Completed Before all Checklist Items are Completed. This is unexpected error.");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    // -----Checklist Exceptions
    @ExceptionHandler(ChecklistItemNotFoundException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleChecklistItemNotFoundException(ChecklistItemNotFoundException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "ChecklistItem Not Found");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ChecklistItemAlreadyExistException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleChecklistItemAlreadyExistException(ChecklistItemAlreadyExistException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Checklist Item Name Already Exist For The Task.");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ChecklistItemAlreadyStartedException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleChecklistItemAlreadyStartedException(ChecklistItemAlreadyStartedException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Checklist Item is Already marked as Started.");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ChecklistItemAlreadyCompletedException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleChecklistAlreadyCompletedException(ChecklistItemAlreadyCompletedException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Checklist Item is already marked as Completed.");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }




  // -----Category Exceptions
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleCategoryNotFoundException(CategoryNotFoundException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Category Not Found");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.NOT_FOUND.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleDuplicateCategoryException(DuplicateCategoryException ex,WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Category Already exist");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.CONFLICT.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());
        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return  new ResponseEntity<>(responseDTO, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "User not Found");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("Invalid Request", errorDetails);
        return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
    }


}
