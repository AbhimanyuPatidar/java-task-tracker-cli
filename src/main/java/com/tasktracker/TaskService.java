package com.tasktracker;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

public class TaskService {
    private TaskManager manager;

    public TaskService(TaskManager manager) {
        this.manager = manager;
    }

    public boolean addTask(int id, String description, Status status) {
        Task task = new Task(id, description);

        if (status != null) {
            task.setStatus(status);
        }

        manager.getTasks().add(task);
        System.out.println("Task added successfully (id=" + task.getId() + ")");
        return true;
    }

    public boolean updateTask(int id, String description, Status status) {
        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found for id=" + id);
            return false;
        }

        task.setDescription(description);
        if (status != null) {
            task.setStatus(status);
        }
        task.setUpdatedAt(LocalDateTime.now());

        System.out.println("Task updated successfully (id=" + id + ")");
        return true;
    }

    public boolean deleteTask(int id) {
        Iterator<Task> iterator = manager.getTasks().iterator();

        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.getId() == id) {
                iterator.remove();
                System.out.println("Task deleted successfully (id=" + id + ")");
                return true;
            }
        }

        System.out.println("Task not found for id=" + id);
        return false;
    }

    public boolean markTaskInProgress(int id) {
        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found for id=" + id);
            return false;
        }

        task.setStatus(Status.IN_PROGRESS);
        task.setUpdatedAt(LocalDateTime.now());
        System.out.println("Task marked in-progress (id=" + id + ")");
        return true;
    }

    public boolean markTaskDone(int id) {
        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found for id=" + id);
            return false;
        }

        task.setStatus(Status.DONE);
        task.setUpdatedAt(LocalDateTime.now());
        System.out.println("Task marked done (id=" + id + ")");
        return true;
    }

    public void listAllTasks() {
        printTasks(manager.getTasks());
    }

    public void listDoneTasks() {
        printTasksByStatus(Status.DONE);
    }

    public void listInProgressTasks() {
        printTasksByStatus(Status.IN_PROGRESS);
    }

    public void listTodoTasks() {
        printTasksByStatus(Status.TODO);
    }

    private void printTasksByStatus(Status status) {
        if (manager.getTasks().isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }

        StringBuilder output = new StringBuilder();
        int count = 0;

        for (Task task : manager.getTasks()) {
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
        for (Task task : manager.getTasks()) {
            if (task.getId() == id) {
                return task;
            }
        }

        return null;
    }
}
