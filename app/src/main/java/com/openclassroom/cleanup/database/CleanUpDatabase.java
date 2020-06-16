package com.openclassroom.cleanup.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassroom.cleanup.database.dao.ProjectDao;
import com.openclassroom.cleanup.database.dao.TaskDao;
import com.openclassroom.cleanup.model.Project;
import com.openclassroom.cleanup.model.Task;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class CleanUpDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile CleanUpDatabase INSTANCE;

    // --- DAO ---
    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    // --- INSTANCE ---
    public static CleanUpDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CleanUpDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CleanUpDatabase.class, "MyDatabase.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(prepopulate())
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // PREPOPULATE
    public static Callback prepopulate(){
        return new Callback() {

            @Override
            public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                super.onDestructiveMigration(db);
                prepopulateWithProjects(db);
                prepopulateWithTasks(db);
            }

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                prepopulateWithProjects(db);
                prepopulateWithTasks(db);
            }
        };
    }

    private static void prepopulateWithProjects(SupportSQLiteDatabase db){
        for (Project project  : Project.getProjectsToPrepopulate()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", project.getId());
            contentValues.put("name", project.getName());
            contentValues.put("color", project.getColor());
            db.insert("Project", OnConflictStrategy.IGNORE, contentValues);
        }

    }

    private static void prepopulateWithTasks(SupportSQLiteDatabase db){
        for (Task task  : Task.getTasksToPrepopulate()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", task.getId());
            contentValues.put("projectId", task.getProjectId());
            contentValues.put("name", task.getName());
            contentValues.put("creationTimestamp", task.getCreationTimestamp());
            db.insert("Task", OnConflictStrategy.IGNORE, contentValues);
        }

    }



}
