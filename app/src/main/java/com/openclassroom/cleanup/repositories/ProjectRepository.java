package com.openclassroom.cleanup.repositories;


import androidx.lifecycle.LiveData;

import com.openclassroom.cleanup.database.dao.ProjectDao;
import com.openclassroom.cleanup.model.Project;

import java.util.List;

public class ProjectRepository {

    private final ProjectDao projectDao;

    public ProjectRepository(ProjectDao projectDao) { this.projectDao = projectDao; }

    // --- GET PROJECT ---
    public LiveData<List<Project>> getAllProjects() { return this.projectDao.getAllProjects(); }
    public LiveData<Project> getProject(long projectId) { return this.projectDao.getProject(projectId); }

    // --- CREATE ---
    public void createProject(Project project){ projectDao.createProject(project); }
}

