package org.example.taskpilot_taskmanager.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;

import org.example.taskpilot_taskmanager.task.repositories.SchemaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class SchemaCache {

    private final SchemaRepository schemaRepository;
    private ObjectMapper mapper;
    private String schemaJson;

    private List<Map<String, Object>> schemaString;


    public SchemaCache(SchemaRepository schemaRepository, ObjectMapper mapper) {
        this.schemaRepository = schemaRepository;
        this.mapper = mapper;
    }

    @PostConstruct
    public void init() throws JsonProcessingException {
        List<Map<String, Object>> schemaInfo = schemaRepository.getSchemaInfo();
        this.schemaJson = mapper.writeValueAsString(schemaInfo);
    }


    public String getSchemaJson() {
        return this.schemaJson;
    }

}

