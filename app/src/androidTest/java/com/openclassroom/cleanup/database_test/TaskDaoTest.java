package com.openclassroom.cleanup.database_test;


import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassroom.cleanup.database.CleanUpDatabase;
import com.openclassroom.cleanup.model.Project;
import com.openclassroom.cleanup.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    // FOR DATA
    private CleanUpDatabase database;

    // DATA SET FOR TEST
    private static long PROJECT_ID = 1;
    private static Project PROJECT_DEMO = new Project(PROJECT_ID, "Projet Tartampion", 0xFFEADAD1);
    private static Task NEW_TASK = new Task(5, PROJECT_ID, "To delete", new Date().getTime());

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, CleanUpDatabase.class)
                .addCallback(CleanUpDatabase.prepopulateWithProjects())
                .addCallback(CleanUpDatabase.prepopulateWithTasks())
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws IOException {
        database.close();
    }

    //-----
    //TESTS
    //-----

    @Test
    public void getProjectFromDb() throws InterruptedException {
        // TEST
        Project project = LiveDataTestUtil.getValue(this.database.projectDao().getProject(PROJECT_ID));
        assertTrue(project.getName().equals(PROJECT_DEMO.getName()) && project.getId() == PROJECT_ID);
    }

    @Test
    public void insertAndGetTasksFromDb() throws InterruptedException {
        // BEFORE : Adding demo tasks
        this.database.taskDao().createTask(NEW_TASK);

        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(5, tasks.size());
    }

    @Test
    public void createAndDeleteTaskFromDb() throws InterruptedException {
        // BEFORE : Adding demo item. Next, get the item added & delete it.
        this.database.taskDao().createTask(NEW_TASK);
        Task taskAdded = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks()).get(4);
        this.database.taskDao().deleteTask(taskAdded.getId());

        //TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.size() < 5);
    }
}
