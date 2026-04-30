package com.tasktracker;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        TaskManager taskManager = new TaskManager();
        
        String command = args[0];
        
        switch (command) {
            case "add":
                // Add task logic
                break;
            case "update":
                // Update task logic
                break;
            case "delete":
                // Delete task logic
                break;
            case "mark-in-progress":
                // Mark task as in progress
                break;
            case "mark-done":
                // Mark task as done
                break;
            case "list":
                // List all tasks logic
                break;
            case "list-done":
                // List all tasks that are done
                break;
            case "list-not-done":
                // List all tasks that are not done
                break;
            case "list-in-progress":
                // List all tasks that are in progress
                break;
            default:
                System.out.println("Unknown command: " + command);
                printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: task-cli <command>");
    }
}