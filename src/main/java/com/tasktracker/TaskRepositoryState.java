package com.tasktracker;

import java.util.List;

public class TaskRepositoryState {
    private int lastId;
    private List<Task> tasks;

    public TaskRepositoryState(int lastId, List<Task> tasks) {
        this.lastId = lastId;
        this.tasks = tasks;
    }

    public int getLastId() {
        return lastId;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
