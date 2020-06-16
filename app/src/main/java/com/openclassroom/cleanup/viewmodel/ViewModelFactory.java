package com.openclassroom.cleanup.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassroom.cleanup.repositories.ProjectRepository;
import com.openclassroom.cleanup.repositories.TaskRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final TaskRepository taskDataSource;
    private final ProjectRepository projectDataSource;
    private final Executor executor;

    ViewModelFactory(TaskRepository taskDataSource, ProjectRepository projectDataSource, Executor executor) {
        this.taskDataSource = taskDataSource;
        this.projectDataSource = projectDataSource;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(taskDataSource, projectDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
