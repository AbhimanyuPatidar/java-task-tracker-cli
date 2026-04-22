// Class to provide task management functionality.

package com.tasktracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private static final Path FILE_PATH = Paths.get("tasks.json");
    private List<Task> tasks; // Arraylist to store all the tasks objects.
    private int lastId; // Will hold the last used task id.

    public TaskManager() {
        this.tasks = new ArrayList<>();
        this.lastId = 0;

        try {
            if (!Files.exists(FILE_PATH)) {
                // Json doesn't exist and needs to be created.

                String initialJson = "{\"lastId\": 0, \"tasks\": []}";

                Files.createFile(FILE_PATH);
                Files.write(FILE_PATH, initialJson.getBytes());

                System.out.println("No task file found.");
                System.out.println("Created new task file at: " + FILE_PATH.toAbsolutePath());
            }
        } catch (IOException ioe) {
            System.err.println("Error initialising task file: " + ioe.getMessage());
        }
    }
}
