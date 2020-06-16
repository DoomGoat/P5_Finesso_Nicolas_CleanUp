package com.openclassroom.cleanup.views;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.cleanup.R;
import com.openclassroom.cleanup.model.Project;
import com.openclassroom.cleanup.model.Task;

import java.lang.ref.WeakReference;
import java.util.List;

public class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // FOR DESIGN
    private final AppCompatImageView imgProject;
    private final TextView lblTaskName;
    private final TextView lblProjectName;
    private final AppCompatImageView imgDelete;

    // FOR DATA
    private WeakReference<TaskAdapter.Listener> callbackWeakRef;


    TaskViewHolder(@NonNull View taskView) {
        super(taskView);

        imgProject = taskView.findViewById(R.id.img_project);
        lblTaskName = taskView.findViewById(R.id.lbl_task_name);
        lblProjectName = taskView.findViewById(R.id.lbl_project_name);
        imgDelete = taskView.findViewById(R.id.img_delete);

    }

    /**
     * Binds a task to the item view.
     *
     * @param task the task to bind in the item view
     */
    void bind(Task task, TaskAdapter.Listener callback, List<Project> projects) {
        Project taskProject = task.getProject(projects);

        this.callbackWeakRef = new WeakReference<>(callback);
        this.lblTaskName.setText(task.getName());
        this.imgDelete.setOnClickListener(this);

        if (taskProject != null) {
            imgProject.setSupportImageTintList(ColorStateList.valueOf(taskProject.getColor()));
            lblProjectName.setText(taskProject.getName());
        } else {
            imgProject.setVisibility(View.INVISIBLE);
            lblProjectName.setText("");
        }

    }

    @Override
    public void onClick(View v) {
        TaskAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }

}