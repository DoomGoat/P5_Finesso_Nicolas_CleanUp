package com.openclassroom.cleanup.viewmodel;

import android.content.Context;

import com.openclassroom.cleanup.database.CleanUpDatabase;
import com.openclassroom.cleanup.repositories.ProjectRepository;
import com.openclassroom.cleanup.repositories.TaskRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    private static TaskRepository provideTaskDataSource(Context context) {
        CleanUpDatabase database = CleanUpDatabase.getInstance(context);
        return new TaskRepository(database.taskDao());
    }

    private static ProjectRepository provideProjectDataSource(Context context) {
        CleanUpDatabase database = CleanUpDatabase.getInstance(context);
        return new ProjectRepository(database.projectDao());
    }

    private static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskRepository dataSourceTask = provideTaskDataSource(context);
        ProjectRepository dataSourceProject = provideProjectDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceTask, dataSourceProject, executor);
    }
}
