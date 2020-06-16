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
                            .addCallback(prepopulateWithProjects())
                            .addCallback(prepopulateWithTasks())
                            .allowMainThreadQueries()
                            .build();
                            //.fallbackToDestructiveMigration()
                }
            }
        }
        return INSTANCE;
    }

    // PREPOPULATE
    public static Callback prepopulateWithProjects(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                for (Project project  : Project.getPrepopulateProject()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", project.getId());
                    contentValues.put("name", project.getName());
                    contentValues.put("color", project.getColor());
                    db.insert("Project", OnConflictStrategy.IGNORE, contentValues);
                }

            }
        };
    }

    public static Callback prepopulateWithTasks(){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                for (Task task  : Task.getPrepopulateTask()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", task.getId());
                    contentValues.put("projectId", task.getProjectId());
                    contentValues.put("name", task.getName());
                    contentValues.put("creationTimestamp", task.getCreationTimestamp());
                    db.insert("Task", OnConflictStrategy.IGNORE, contentValues);
                }

            }
        };
    }

}
