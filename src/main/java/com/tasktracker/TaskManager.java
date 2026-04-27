// Class to provide task management functionality.

package com.tasktracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TaskManager {
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final Path FILE_PATH = Paths.get(PROJECT_ROOT, "data", "tasks.json");

    private List<Task> tasks; // Arraylist to store all the tasks objects.
    private int lastId; // Will hold the last used task id.

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.lastId = 0;

        ensureFolderExists();
        ensureFileExists();
        loadLastId();
        loadTasks();
    }

    private void ensureFolderExists() {
        try {
            Path parentDir = FILE_PATH.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
        } catch (IOException ioe) {
            System.err.println("Failed to create data directory: " + ioe.getMessage());
        }
    }

    private void ensureFileExists() {
        try {
            if (!Files.exists(FILE_PATH)) {
                String initialJson = "{\"lastId\": 0, \"tasks\": []}";

                Files.write(FILE_PATH, initialJson.getBytes());

                System.out.println("Initialised new storage at: " + FILE_PATH.toAbsolutePath());
            }
        } catch (IOException ioe) {
            System.err.println("Failed to create tasks file: " + ioe.getMessage());
        }
    }

    private void loadLastId() {
        try {
            if (!Files.exists(FILE_PATH)) return;

            byte[] encoded = Files.readAllBytes(FILE_PATH);
            String json = new String(encoded, StandardCharsets.UTF_8);

            // Logic to parse the string
            int keyIndex = json.indexOf("\"lastId\"");
            int colonIndex = json.indexOf(":", keyIndex);
            int commaIndex = json.indexOf(",", colonIndex);
            int braceIndex = json.indexOf("}", colonIndex);

            // Choose the end of the lastId integer
            int endIndex;
            if (commaIndex != -1 && (braceIndex == -1 || commaIndex < braceIndex)) {
                endIndex = commaIndex;
            } else {
                endIndex = braceIndex;
            }

            String value = json.substring(colonIndex+1, endIndex).trim();
            this.lastId = Integer.parseInt(value);
        } catch (IOException ioe) {
            System.out.println("Error loading tasks: " + ioe.getMessage());
        }
    }

    private void loadTasks() {
        String tasksArray = extractTasksString();

        if (tasksArray == null) return;
        
        List<String> taskTokens = extractTaskTokens(tasksArray);

        if (taskTokens.size() == 0) return;

        parseTaskTokens(taskTokens);
    }

    private String extractTasksString() {
        String tasksArray = null;
        try {
            // Extract the json string from tasks.json file
            String json = new String(Files.readAllBytes(FILE_PATH), StandardCharsets.UTF_8);

            // Find the array of tasks bound by []
            int arrayStart = json.indexOf("[");
            int arrayEnd = json.indexOf("]");
            if (arrayStart == -1 || arrayEnd == -1) return null;

            tasksArray = json.substring(arrayStart +1, arrayEnd);
        } catch (IOException ioe) {
            System.err.println("Unable to read json file: " + ioe.getMessage());
        }
        
        return tasksArray;
    }

    private List<String> extractTaskTokens(String tasksArray) {
        // Seperate each task acc to braces
        List<String> taskTokens = new ArrayList<>();

        int start = 0;
        while ((start = tasksArray.indexOf("{", start)) != -1) {
            int end = tasksArray.indexOf("}", start);

            String taskToken = tasksArray.substring(start+1, end);
            taskTokens.add(taskToken);
            
            start = end+1;
        }

        return taskTokens;
    }

    private void parseTaskTokens(List<String> taskTokens) {
        Iterator<String> iterator = taskTokens.iterator();

        while (iterator.hasNext()) {
            String taskToken = iterator.next();
            Task task = parseSingleTask(taskToken);
            tasks.add(task);
        }
    }

    private Task parseSingleTask(String taskToken) {
        Task task = new Task();

        int idIdx = taskToken.indexOf("\"id\"");
        int descIdx = taskToken.indexOf("\"description\"");
        int statusIdx = taskToken.indexOf("\"status\"");
        int createdAtIdx = taskToken.indexOf("\"createdAt\"");
        int updatedAtIdx = taskToken.indexOf("\"updatedAt\"");

        int start = -1, end = -1;
        
        // Extract and set value of id
        start = idIdx + 5;
        end = descIdx;
        String idStr = taskToken.substring(start, end).trim();
        idStr = idStr.substring(0, idStr.length() -1);
        int id = Integer.parseInt(idStr);
        task.setId(id);

        // Extract and set value of description
        start = descIdx + 14;
        end = statusIdx;
        String descStr = taskToken.substring(start, end).trim();
        descStr = descStr.substring(1, descStr.length() -2);
        task.setDescription(descStr);

        // Extract and set value of status
        start = statusIdx + 9;
        end = createdAtIdx;
        String statusStr = taskToken.substring(start, end).trim();
        statusStr = statusStr.substring(1, statusStr.length() -2);
        task.setStatus(Status.fromJsonValue(statusStr));

        // Extract and set value of createdAt
        start = createdAtIdx + 12;
        if (updatedAtIdx == -1) end = taskToken.length();
        else end = updatedAtIdx;
        String createdAtStr = taskToken.substring(start, end).trim();
        if (updatedAtIdx == -1) createdAtStr = createdAtStr.substring(1, createdAtStr.length() -1);
        else createdAtStr = createdAtStr.substring(1, createdAtStr.length() -2);
        task.setCreatedAt(LocalDateTime.parse(createdAtStr));

        // Extract and set value of updatedAt if present
        if (updatedAtIdx != -1) {
            start = updatedAtIdx + 12;
            end = taskToken.length();
            String updatedAtStr = taskToken.substring(start, end).trim();
            updatedAtStr = updatedAtStr.substring(1, updatedAtStr.length() -1);
            task.setUpdatedAt(LocalDateTime.parse(updatedAtStr));
        } else task.setUpdatedAt(null);

        return task;
    }
}
