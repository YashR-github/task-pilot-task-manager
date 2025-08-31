package org.example.taskpilot_taskmanager.notifications.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO implements Serializable {
    private String to;
    private String subject;
    private String template;          // optional template name
    private Map<String, Object> model; // optional model for template
    private String content;           // optional simple content text/html

}

