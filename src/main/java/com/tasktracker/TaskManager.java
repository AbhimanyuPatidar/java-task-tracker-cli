// Class to provide task management functionality.

package com.tasktracker;

import java.util.List;

public class TaskManager {
    private List<Task> tasks;
    private int lastId;

    private TaskService taskService;
    private TaskRepository repository;

    public TaskManager() {
        this(new FileTaskRepository());
    }

    public TaskManager(TaskRepository repository) {
        this.repository = repository;
        this.taskService = new TaskService(this);

        TaskRepositoryState state = repository.loadState();
        this.lastId = state.getLastId();
        this.tasks = state.getTasks();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public void add(String description, Status status) {
        int nextId = ++lastId;
        if (taskService.addTask(nextId, description, status)) {
            repository.saveState(new TaskRepositoryState(lastId, tasks));
        }
    }

    public void update(int id, String description, Status status) {
        if (taskService.updateTask(id, description, status)) {
            repository.saveState(new TaskRepositoryState(lastId, tasks));
        }
    }

    public void delete(int id) {
        if (taskService.deleteTask(id)) {
            repository.saveState(new TaskRepositoryState(lastId, tasks));
        }
    }

    public void markInProgress(int id) {
        if (taskService.markTaskInProgress(id)) {
            repository.saveState(new TaskRepositoryState(lastId, tasks));
        }
    }

    public void markDone(int id) {
        if (taskService.markTaskDone(id)) {
            repository.saveState(new TaskRepositoryState(lastId, tasks));
        }
    }

    public void listAll() {
        taskService.listAllTasks();
    }

    public void listDone() {
        taskService.listDoneTasks();
    }

    public void listInProgress() {
        taskService.listInProgressTasks();
    }

    public void listTodo() {
        taskService.listTodoTasks();
    }
}