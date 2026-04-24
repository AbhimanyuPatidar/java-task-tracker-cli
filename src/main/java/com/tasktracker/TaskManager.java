// Class to provide task management functionality.

package com.tasktracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
    }

    private void ensureFolderExists() {
        try {
            Path parentDir = FILE_PATH.getParent();
            if (parentDir != null && Files.exists(parentDir)) {
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
}
