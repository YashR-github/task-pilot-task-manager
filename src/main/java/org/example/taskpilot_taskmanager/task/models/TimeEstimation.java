package org.example.taskpilot_taskmanager.task.models;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.Objects;


@Embeddable
@Getter
@Setter
public class TimeEstimation {
    @Column(nullable=false)
    private Long days=0L;

    @Column(nullable=false)
    private Long hours=0L;

    @Column(nullable=false)
    private Long minutes=0L;


    //for extra safety, since days, hours, minutes may be null, requireNonNullElse sets default to 0L
    public Duration toDuration(){
        return Duration.ofDays(Objects.requireNonNullElse(this.days,0L))
                .plusHours(Objects.requireNonNullElse(this.hours,0L))
                .plusMinutes(Objects.requireNonNullElse(this.minutes,0L));
    }

}
