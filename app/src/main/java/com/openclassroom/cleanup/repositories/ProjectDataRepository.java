package com.openclassroom.cleanup.repositories;


import androidx.lifecycle.LiveData;

import com.openclassroom.cleanup.database.dao.ProjectDao;
import com.openclassroom.cleanup.model.Project;

import java.util.List;

public class ProjectDataRepository {

    private final ProjectDao projectDao;

    public ProjectDataRepository(ProjectDao projectDao) { this.projectDao = projectDao; }

    // --- GET PROJECT ---
    public LiveData<List<Project>> getAllProjects() { return this.projectDao.getAllProjects(); }
    public LiveData<Project> getProject(long projectId) { return this.projectDao.getProject(projectId); }
}
