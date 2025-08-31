package org.example.taskpilot_taskmanager.task.dtos;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeEstimateDTO {

    @Max(value=365,message="Days must be <= 365. Please correct Expected Time.")
    @Min(value=0,message="Days must be >= 0. Please correct Expected Time.")
    private Long days;

    @Max(value=24,message="Hours must be <= 24. Please correct Expected Time.")
    @Min(value=0,message="Hours must be >= 0. Please correct Expected Time.")
    private Long hours;

    @Max(value=60,message="Minutes must be <= 60. Please correct Expected Time.")
    @Min(value=0,message="Minutes must be >= 0. Please correct Expected Time.")
    private Long minutes;
}
