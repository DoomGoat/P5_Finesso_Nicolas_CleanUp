package com.openclassroom.cleanup.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Comparator;
import java.util.Date;
import java.util.List;



@Entity(foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "projectId"))
public class Task {

    /**
     * The unique identifier of the task
     */
    @PrimaryKey(autoGenerate = true)
    private long id;

    /**
     * The unique identifier of the project associated to the task
     */
    private long projectId;

    /**
     * The name of the task
     */
    private String name;

    /**
     * The timestamp when the task has been created
     */
    private long creationTimestamp;

    /**
     * Instantiates a new Task.
     *
     * @param id                the unique identifier of the task to set
     * @param projectId         the unique identifier of the project associated to the task to set
     * @param name              the name of the task to set
     * @param creationTimestamp the timestamp when the task has been created to set
     */
    public Task(long id, long projectId, @NonNull String name, long creationTimestamp) {
        this.setId(id);
        this.setProjectId(projectId);
        this.setName(name);
        this.setCreationTimestamp(creationTimestamp);
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }


    private void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getProjectId() {
        return projectId;
    }

    public Project getProject(List<Project> projects) {
        return Project.getProjectById(getProjectId(), projects);
    }


    @NonNull
    public String getName() {
        return name;
    }

    private void setName(@NonNull String name) {
        this.name = name;
    }


    private void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }


    public static Task[] getPrepopulateTask() {
        return new Task[]{
        new Task(1, 1, "task 1", new Date().getTime()),
        new Task(2, 2, "task 2", new Date().getTime()),
        new Task(3, 3, "task 3", new Date().getTime()),
        new Task(4, 4, "task 4", new Date().getTime())
        };
    }

    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.name.compareTo(right.name);
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.name.compareTo(left.name);
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (right.creationTimestamp - left.creationTimestamp);
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (left.creationTimestamp - right.creationTimestamp);
        }
    }
}
