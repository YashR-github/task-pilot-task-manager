package org.example.taskpilot_taskmanager.task.repositories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SchemaRepository {
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public SchemaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getSchemaInfo() {
        String sql = """
        SELECT table_name, column_name, data_type
        FROM information_schema.columns
        WHERE table_schema = DATABASE() 
        ORDER BY table_name, ordinal_position
    """;
        return jdbcTemplate.queryForList(sql);
    }

}
