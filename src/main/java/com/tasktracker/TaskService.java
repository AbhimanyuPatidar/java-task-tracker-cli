package com.tasktracker;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

public class TaskService {
    public void addTask(int id, String description, Status status) {
        Task task = new Task(id, description);

        if (status != null) {
            task.setStatus(status);
        }

        TaskManager.tasks.add(task);
        System.out.println("Task added successfully (id=" + task.getId() + ")");
    }

    public void updateTask(int id, String description, Status status) {
        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found for id=" + id);
            return;
        }

        task.setDescription(description);
        if (status != null) {
            task.setStatus(status);
        }
        task.setUpdatedAt(LocalDateTime.now());

        System.out.println("Task updated successfully (id=" + id + ")");
    }

    public void deleteTask(int id) {
        Iterator<Task> iterator = TaskManager.tasks.iterator();

        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getId() == id) {
                iterator.remove();
                System.out.println("Task deleted successfully (id=" + id + ")");
                return;
            }
        }

        System.out.println("Task not found for id=" + id);
    }

    public void markTaskInProgress(int id) {
        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found for id=" + id);
            return;
        }

        task.setStatus(Status.IN_PROGRESS);
        task.setUpdatedAt(LocalDateTime.now());
        System.out.println("Task marked in-progress (id=" + id + ")");
    }

    public void markTaskDone(int id) {
        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found for id=" + id);
            return;
        }

        task.setStatus(Status.DONE);
        task.setUpdatedAt(LocalDateTime.now());
        System.out.println("Task marked done (id=" + id + ")");
    }

    public void listAllTasks() {
        printTasks(TaskManager.tasks);
    }

    public void listDoneTasks() {
        printTasksByStatus(Status.DONE);
    }

    public void listNotDoneTasks() {
        if (TaskManager.tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        StringBuilder output = new StringBuilder();
        int count = 0;

        for (Task task : TaskManager.tasks) {
            if (task.getStatus() != Status.DONE) {
                output.append(formatTask(task)).append(System.lineSeparator());
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.print(output.toString());
    }

    public void listInProgressTasks() {
        printTasksByStatus(Status.IN_PROGRESS);
    }

    private void printTasksByStatus(Status status) {
        if (TaskManager.tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        StringBuilder output = new StringBuilder();
        int count = 0;

        for (Task task : TaskManager.tasks) {
            if (task.getStatus() == status) {
                output.append(formatTask(task)).append(System.lineSeparator());
                count++;
            }
        }

        if (count == 0) {
            System.out.println("No tasks found.");
            return;
        }

        System.out.print(output.toString());
    }

    private void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        StringBuilder output = new StringBuilder();

        for (Task task : tasks) {
            output.append(formatTask(task)).append(System.lineSeparator());
        }

        System.out.print(output.toString());
    }

    private String formatTask(Task task) {
        return "[" + task.getId() + "] " + task.getDescription() + " (" + task.getStatus().getDisplayValue() + ")";
    }

    private Task findTaskById(int id) {
        for (Task task : TaskManager.tasks) {
            if (task.getId() == id) {
                return task;
            }
        }

        return null;
    }
}
