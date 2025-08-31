package org.example.taskpilot_taskmanager.common.controlleradvice;


import org.example.taskpilot_taskmanager.common.dtos.ResponseDTO;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class) // exception handler for controller @Valid check
    public ResponseEntity<ResponseDTO<Map<String,String>>> handleControllerValidation(MethodArgumentNotValidException ex){
        Map<String,String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField,
                                          FieldError::getDefaultMessage,
                                         (a,b) -> a // in case of duplicate field errors
                       ));
        return ResponseEntity.badRequest().body(new ResponseDTO<>("Validation failure", errors));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Map<String, Object>>> handleGenericException(Exception ex, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("error", "Internal Server Error");
        errorDetails.put("details", ex.getMessage());
        errorDetails.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDetails.put("path", ((ServletWebRequest) request).getRequest().getRequestURI());

        ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>("An unexpected error occurred", errorDetails);
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
