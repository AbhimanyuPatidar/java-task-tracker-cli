package com.tasktracker;

public interface TaskRepository {
    TaskRepositoryState loadState();
    void saveState(TaskRepositoryState state);
}
