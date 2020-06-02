package com.openclassroom.cleanup.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.openclassroom.cleanup.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void createProject(Project project);

    @Query("SELECT * FROM Project WHERE id = :projectId")
    LiveData<Project> getProject(long projectId);

    @Query("SELECT * FROM Project")
    LiveData <List<Project>> getAllProjects();

}
