package com.openclassroom.cleanup.repositories;


import androidx.lifecycle.LiveData;

import com.openclassroom.cleanup.database.dao.TaskDao;
import com.openclassroom.cleanup.model.Task;

import java.util.List;

public class TaskRepository {

    private final TaskDao taskDao;

    public TaskRepository(TaskDao taskDao) { this.taskDao = taskDao; }

    // --- GET ---
    public LiveData<List<Task>> getAllTasks(){ return this.taskDao.getAllTasks(); }

    // --- CREATE ---
    public void createTask(Task task){ taskDao.createTask(task); }

    // --- DELETE ---
    public void deleteTask(long taskId){ taskDao.deleteTask(taskId); }

}
