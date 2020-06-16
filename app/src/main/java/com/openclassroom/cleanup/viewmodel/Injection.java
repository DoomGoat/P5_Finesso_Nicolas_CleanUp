package com.openclassroom.cleanup.viewmodel;

import android.content.Context;

import com.openclassroom.cleanup.database.CleanUpDatabase;
import com.openclassroom.cleanup.repositories.ProjectDataRepository;
import com.openclassroom.cleanup.repositories.TaskDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    private static TaskDataRepository provideTaskDataSource(Context context) {
        CleanUpDatabase database = CleanUpDatabase.getInstance(context);
        return new TaskDataRepository(database.taskDao());
    }

    private static ProjectDataRepository provideProjectDataSource(Context context) {
        CleanUpDatabase database = CleanUpDatabase.getInstance(context);
        return new ProjectDataRepository(database.projectDao());
    }

    private static Executor provideExecutor(){ return Executors.newSingleThreadExecutor(); }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        TaskDataRepository dataSourceTask = provideTaskDataSource(context);
        ProjectDataRepository dataSourceProject = provideProjectDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceTask, dataSourceProject, executor);
    }
}
