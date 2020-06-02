package com.openclassroom.cleanup.repositories;


import androidx.lifecycle.LiveData;

import com.openclassroom.cleanup.database.dao.TaskDao;
import com.openclassroom.cleanup.model.Task;

import java.util.List;

public class TaskDataRepository {

    private final TaskDao taskDao;

    public TaskDataRepository(TaskDao taskDao) { this.taskDao = taskDao; }

    // --- GET ---
    public LiveData<List<Task>> getAllTasks(){ return this.taskDao.getAllTasks(); }
    public LiveData<List<Task>> getTaskFromProjectId(long projectId){ return this.taskDao.getTaskFromProjectId(projectId); }

    // --- CREATE ---
    public void createTask(Task task){ taskDao.createTask(task); }

    // --- DELETE ---
    public void deleteTask(long taskId){ taskDao.deleteTask(taskId); }

    // --- UPDATE ---
    // public void updateTask(Task task){ taskDao.updateTask(task); }

}
