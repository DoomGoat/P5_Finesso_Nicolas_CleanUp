package com.openclassroom.cleanup.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassroom.cleanup.R;
import com.openclassroom.cleanup.model.Project;
import com.openclassroom.cleanup.model.Task;

import java.util.ArrayList;
import java.util.List;


public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    // CALLBACK
    public interface Listener { void onClickDeleteButton(int position); }
    private final Listener callback;

    // FOR DATA
    private List<Task> tasks;
    private List<Project> projects = new ArrayList<>();



    // CONSTRUCTOR
    public TaskAdapter(Listener callback) {
        this.tasks = new ArrayList<>();
        this.callback = callback;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder( ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_task, viewGroup, false);

        return new TaskViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder viewHolder, int position) {
        viewHolder.bind(this.tasks.get(position), this.callback, projects);
    }

    @Override
    public int getItemCount() {
        return this.tasks.size();
    }

    public Task getTask(int position){
        return this.tasks.get(position);
    }

    public void updateData(List<Task> tasks, List<Project> projects){
        this.projects = projects;
        this.tasks = tasks;
        this.notifyDataSetChanged();
    }

}
