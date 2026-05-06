package com.tasktracker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileTaskRepository implements TaskRepository {
    private static final String PROJECT_ROOT = System.getProperty("user.dir");
    private static final Path FILE_PATH = Paths.get(PROJECT_ROOT, "data", "tasks.json");

    @Override
    public TaskRepositoryState loadState() {
        ensureFolderExists();
        ensureFileExists();

        int lastId = loadLastId();
        List<Task> tasks = loadTasks();

        return new TaskRepositoryState(lastId, tasks);
    }

    @Override
    public void saveState(TaskRepositoryState state) {
        try {
            ensureFolderExists();

            StringBuilder tasksJson = new StringBuilder();
            List<Task> tasks = state.getTasks();
            for (int i = 0; i < tasks.size(); i++) {
                if (i > 0) {
                    tasksJson.append(",\n");
                }
                tasksJson.append(serializeTask(tasks.get(i)));
            }

            String json = "{\"lastId\": " + state.getLastId() + ", \"tasks\": [" + tasksJson.toString() + "]}";
            Files.write(FILE_PATH, json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ioe) {
            System.err.println("Failed to save tasks: " + ioe.getMessage());
        }
    }

    private String serializeTask(Task task) {
        StringBuilder sb = new StringBuilder();
        String escapedDescription = task.getDescription().replace("{", "\0").replace("}", "\7");
        sb.append("{\n");
            sb.append("\"id\":\n").append(task.getId()).append(",\n");
            sb.append("\"description\":\n\"").append(escapedDescription).append("\",\n");
            sb.append("\"status\":\n\"").append(task.getStatus().getDisplayValue()).append("\",\n");
            sb.append("\"createdAt\":\n\"").append(task.getCreatedAt()).append("\"");
        if (task.getUpdatedAt() != null) {
                sb.append(",\n\"updatedAt\":\n\"").append(task.getUpdatedAt()).append("\"");
        }
        sb.append("\n}");
        return sb.toString();
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

    private int loadLastId() {
        try {
            if (!Files.exists(FILE_PATH)) return 0;

            byte[] encoded = Files.readAllBytes(FILE_PATH);
            String json = new String(encoded, StandardCharsets.UTF_8);

            int keyIndex = json.indexOf("\"lastId\"");
            int colonIndex = json.indexOf(":", keyIndex);
            int commaIndex = json.indexOf(",", colonIndex);
            int braceIndex = json.indexOf("}", colonIndex);

            int endIndex;
            if (commaIndex != -1 && (braceIndex == -1 || commaIndex < braceIndex)) {
                endIndex = commaIndex;
            } else {
                endIndex = braceIndex;
            }

            String value = json.substring(colonIndex + 1, endIndex).trim();
            return Integer.parseInt(value);
        } catch (IOException ioe) {
            System.err.println("Error loading lastId: " + ioe.getMessage());
            return 0;
        }
    }

    private List<Task> loadTasks() {
        String tasksArray = extractTasksString();
        if (tasksArray == null) return new ArrayList<>();

        List<String> taskTokens = extractTaskTokens(tasksArray);
        if (taskTokens.isEmpty()) return new ArrayList<>();

        List<Task> tasks = new ArrayList<>();
        for (String token : taskTokens) {
            tasks.add(parseSingleTask(token));
        }
        return tasks;
    }

    private String extractTasksString() {
        try {
            String json = new String(Files.readAllBytes(FILE_PATH), StandardCharsets.UTF_8);

            int arrayStart = json.indexOf("[");
            int arrayEnd = json.length() - 2;
            if (arrayStart == -1 || arrayEnd == -1) return null;

            return json.substring(arrayStart + 1, arrayEnd);
        } catch (IOException ioe) {
            System.err.println("Unable to read json file: " + ioe.getMessage());
            return null;
        }
    }

    private List<String> extractTaskTokens(String tasksArray) {
        // Seperate each task acc to braces
        List<String> taskTokens = new ArrayList<>();

        int start = 0;
        while ((start = tasksArray.indexOf("{", start)) != -1) {
            int end = tasksArray.indexOf("}", start);
            taskTokens.add(tasksArray.substring(start + 1, end));
            start = end + 1;
        }

        return taskTokens;
    }

    private Task parseSingleTask(String taskToken) {
        Task task = new Task();

            int idIdx = taskToken.indexOf("\n\"id\":\n");
            int descIdx = taskToken.indexOf("\n\"description\":\n");
            int statusIdx = taskToken.indexOf("\n\"status\":\n");
            int createdAtIdx = taskToken.indexOf("\n\"createdAt\":\n");
            int updatedAtIdx = taskToken.indexOf("\n\"updatedAt\":\n");

        int start, end;

        // Extract and set value of id
            start = idIdx + 7;
        end = descIdx;
        String idStr = taskToken.substring(start, end).trim();
        idStr = idStr.substring(0, idStr.length() - 1);
        task.setId(Integer.parseInt(idStr));

        // Extract and set value of description
            start = descIdx + 16;
        end = statusIdx;
        String descStr = taskToken.substring(start, end).trim();
        descStr = descStr.substring(1, descStr.length() - 2);
        descStr = descStr.replace("\0", "{").replace("\7", "}");
        task.setDescription(descStr);

        // Extract and set value of status
            start = statusIdx + 11;
        end = createdAtIdx;
        String statusStr = taskToken.substring(start, end).trim();
        statusStr = statusStr.substring(1, statusStr.length() - 2);
        task.setStatus(Status.fromJsonValue(statusStr));

        // Extract and set value of createdAt
            start = createdAtIdx + 14;
        if (updatedAtIdx == -1) end = taskToken.length();
        else end = updatedAtIdx;
        String createdAtStr = taskToken.substring(start, end).trim();
        if (updatedAtIdx == -1) createdAtStr = createdAtStr.substring(1, createdAtStr.length() - 1);
        else createdAtStr = createdAtStr.substring(1, createdAtStr.length() - 2);
        task.setCreatedAt(LocalDateTime.parse(createdAtStr));

        // Extract and set value of updatedAt if present
        if (updatedAtIdx != -1) {
                start = updatedAtIdx + 14;
            end = taskToken.length();
            String updatedAtStr = taskToken.substring(start, end).trim();
            updatedAtStr = updatedAtStr.substring(1, updatedAtStr.length() - 1);
            task.setUpdatedAt(LocalDateTime.parse(updatedAtStr));
        } else {
            task.setUpdatedAt(null);
        }

        return task;
    }
}
