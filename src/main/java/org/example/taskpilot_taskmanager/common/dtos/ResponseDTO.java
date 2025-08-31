package org.example.taskpilot_taskmanager.common.dtos;


import lombok.Getter;


@Getter
//@Setter  usually not required
public class ResponseDTO<T> {
    private String message;
    private T data;

    public ResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }

}


