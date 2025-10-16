package org.example.taskpilot_taskmanager.task.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import org.example.taskpilot_taskmanager.user.util.AuthenticatedUserUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GeminiIntegrationService implements ChatbotIntegrationService {
    private final SchemaCache schemaCache;
    private final Client client;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper mapper;
    private final AuthenticatedUserUtil authenticatedUserUtil;

    GeminiIntegrationService(@Value("${GEMINI_API_KEY}") String Api_Key, SchemaCache schemaCache, JdbcTemplate jdbcTemplate, ObjectMapper mapper, AuthenticatedUserUtil authenticatedUserUtil) {
        this.client = Client.builder().apiKey(Api_Key).build();
        this.schemaCache = schemaCache;
        this.jdbcTemplate = jdbcTemplate;
        this.mapper= mapper;
        this.authenticatedUserUtil = authenticatedUserUtil;
    }

    public String generateResponse(String userQuery) throws JsonProcessingException {

        String rawQuery = generateSqlQuery(userQuery);
        String sanitizedQuery = rawQuery
                .replaceAll("(?i)```sql", "")
                .replaceAll("```", "")
                .trim();
        System.out.println("Generated SQL Query: "+sanitizedQuery);

        //execute query
        Object response= executeDynamicQuery(sanitizedQuery);

        //respond back to user
        String result = mapper.writeValueAsString(response);
        System.out.println("Generated Query execution Response: "+ result);
        String queryResponse =  generateUserQueryResponse(result,userQuery);

        return queryResponse;

    }



    public String generateSqlQuery(String userQuery){
        Long userId = authenticatedUserUtil.getCurrentUser().getId();

        String schemaJson = schemaCache.getSchemaJson();
        String prompt = """
                You are an expert SQL query generator. Your task is to fetch the most appropriate and relevant data which could be sufficient for the next AI prompt to decode based on data  and
                provide final answer to the user. Priority is correct and runnable SQL rather than creating non runnable sql for providing all the data that user requires.
                Based on the following database schema:
                %s
                Reason well, exclude and generate a well curated high quality correct and errorfree SQL query strictly as per column names in the schema to answer the following question:
                %s
                Note- You must only generate SQL that queries or modifies data for the currently authenticated user.
                The user's ID is: %d.
                All SQL must include WHERE user_id = %d when applicable.Make sure that the SQL query is syntactically and logically correct and should run without any errors.
                
                Remember- Only output the SQL query,without any explanations or additional text.
                """.formatted(userQuery, schemaJson,userId,userId);
        // todo authorized data access crucial, if unauthorized return text response invalid request, please try something else

        try {
            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-2.5-flash",
                            prompt,
                            null);

            return response.text();
        } catch (Exception e) {
            System.out.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return "Failed to generate content";
        }
    }


    public Object executeDynamicQuery(String sqlQuery) {
    String firstWord= sqlQuery.trim().split("\\s+")[0].toLowerCase();
        switch (firstWord) {
            case "select":
                // Returns list of rows, each row is a map of column -> value
                return jdbcTemplate.queryForList(sqlQuery);

            case "insert":
            case "update":
            case "delete":
                // Returns count of affected rows
                int rowsAffected = jdbcTemplate.update(sqlQuery);
                return Map.of(
                        "status", "success",
                        "rowsAffected", rowsAffected);

            case "create":
            case "alter":
            case "drop":
                // Execute schema changes
                jdbcTemplate.execute(sqlQuery);
                return Map.of(
                        "status", "success",
                        "message", "DDL executed successfully");

            default:
                throw new IllegalArgumentException("Unsupported query type: " + firstWord);
    }

    }

    public String generateUserQueryResponse(String result,String userQuery){
        Long userId = authenticatedUserUtil.getCurrentUser().getId();
        String finalPrompt = """
            You are a support assistant. Consider this as User Query:
            %s
        
            After fetching from the database, SQL Executed Response got is :
            %s
        
            Now respond to the user in a natural, concise manner. You must only provide or respond for data that the currently authenticated user has authority over. 
            Any other requests or info asked should be handled wisely with appropriate support assistant tone stating that the data that can be 
            provided could only be related with the user and not any other user or system or application info. So ensure no private data unrelated to the user 
            should be sent in response.
           
           The user's ID is: %d.
           Note- Answer in general user understandable format to the data received.
            """.formatted(userQuery,result,userId);

        try {
            GenerateContentResponse response =
                    client.models.generateContent(
                            "gemini-2.5-flash",
                            finalPrompt,
                            null);

            return response.text();
        } catch (Exception e) {
            System.out.println("Error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return "Failed to generate content";
        }


    }

}
