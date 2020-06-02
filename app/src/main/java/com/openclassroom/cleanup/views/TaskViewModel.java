package com.openclassroom.cleanup.views;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.openclassroom.cleanup.model.Project;
import com.openclassroom.cleanup.model.Task;
import com.openclassroom.cleanup.repositories.ProjectDataRepository;
import com.openclassroom.cleanup.repositories.TaskDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class TaskViewModel extends ViewModel {

    // REPOSITORIES
    private final TaskDataRepository taskDataSource;
    private final ProjectDataRepository projectDataSource;
    private final Executor executor;

    // FOR DATA
    public TaskViewModel(TaskDataRepository taskDataSource, ProjectDataRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
    }


    // -------------
    // FOR PROJECT
    // -------------

    public LiveData<List<Project>> getAllProjects () {
        return projectDataSource.getAllProjects();
    }


    // -------------
    // FOR TASK
    // -------------

    public LiveData<List<Task>> getAllTasks () {
        return taskDataSource.getAllTasks();
    }


    public void createTask(Task task) {
        executor.execute(() -> taskDataSource.createTask(task));
    }

    public void deleteTask(long taskId) {
        executor.execute(() -> taskDataSource.deleteTask(taskId));
    }

}