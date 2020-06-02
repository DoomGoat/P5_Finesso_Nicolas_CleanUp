package com.openclassroom.cleanup.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.openclassroom.cleanup.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task ")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM Task WHERE projectId = :projectId")
    LiveData<List<Task>> getTaskFromProjectId(long projectId);


    @Insert
    void createTask(Task task);

    @Query("DELETE FROM Task WHERE id = :taskId")
    void deleteTask(long taskId);
}
